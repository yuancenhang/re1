package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.domain.Clue;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.service.ClueActivityRelationService;
import com.hang.crm.work.service.ClueService;
import com.hang.crm.work.service.impl.ActivityServiceImpl;
import com.hang.crm.work.service.impl.ClueActivityRelationServiceImpl;
import com.hang.crm.work.service.impl.ClueServiceImpl;
import com.hang.crm.work.vo.PageVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/clue/getUserList.sv".equals(path)) {
            getUserList(request, response);
        } else if ("/work/clue/saveClue.sv".equals(path)) {
            saveClue(request, response);
        }else if ("/work/clue/getClueList.sv".equals(path)) {
            getClueList(request, response);
        }else if ("/work/clue/selectById.sv".equals(path)) {
            selectById(request, response);
        }else if ("/work/clue/getActivityList.sv".equals(path)) {
            getActivityList(request, response);
        }else if ("/work/clue/saveguanlian.sv".equals(path)) {
            saveguanlian(request, response);
        }else if ("/work/clue/getguanlianActivityList.sv".equals(path)) {
            getguanlianActivityList(request, response);
        }else if ("/work/clue/jieChuGuanLian.sv".equals(path)) {
            deleteById(request, response);
        }else if ("/work/clue/convert.sv".equals(path)) {
            selectById2(request, response);
        }else if ("/work/clue/getActivityListByName.sv".equals(path)) {
            getActivityListByName(request, response);
        }else if ("/work/clue/getAllActivityList.sv".equals(path)) {
            getAllActivityList(request, response);
        }else if ("/work/clue/zhuanHuan.sv".equals(path)) {
            zhuanHuan(request, response);
        }
    }
    /**
     * 把线索转换成客户和联系人
     * @param request  请求对象
     * @param response 响应对象
     */
    private void zhuanHuan(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String clueId = request.getParameter("clueId");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        Tran tran = null;
        String flag = request.getParameter("flag");
        if ("yes".equals(flag)){
            //要创建交易
            String activityId = request.getParameter("activityId");
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String source = request.getParameter("source");
            String createTime = UtilOne.getTime();
            tran = new Tran();
            tran.setId(UtilOne.getUUID());
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setSource(source);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
        }
        ClueService service = (ClueService) UtilOne.getProxyOfCommit(new ClueServiceImpl());
        boolean ok = service.convert(clueId,tran,createBy);
        System.out.println("===========================全部成功吗？============"+ok);
        if (ok) response.sendRedirect(request.getContextPath() + "/work/clue/index.jsp");
    }

    /**
     * 获取所有的市场活动，分页，动态条件
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getAllActivityList(HttpServletRequest request, HttpServletResponse response) {
        String no = request.getParameter("pageNo");
        int pageNo = Integer.parseInt(no);
        String size = request.getParameter("pageSize");
        int pageSize = Integer.parseInt(size);
        pageNo = (pageNo-1)*pageSize;
        String activityName = request.getParameter("activityName");
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("name",activityName);
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        PageVo<Activity> vo = service.getActivityList(map);
        UtilOne.printJson(response,vo);
    }

    /**
     * 根据活动name查找未和某线索关联的活动
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String aname = request.getParameter("aname");
        String cid = request.getParameter("cid");
        Map<String,Object> map = new HashMap<>();
        map.put("aname",aname);
        map.put("cid",cid);
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        List<Activity> list = service.getActivityListByName(map);
        UtilOne.printJson(response,list);
    }
    /**
     * 根据ID获取线索的详细信息，然后转发，加载convert。jsp页面
     * @param request  请求对象
     * @param response 响应对象
     */
    private void selectById2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String clueId = request.getParameter("clueId");
        ClueService service = (ClueService) UtilOne.getProxyOfCommit(new ClueServiceImpl());
        Clue clue = service.selectById(clueId);
        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/work/clue/convert.jsp").forward(request,response);
    }

    /**
     * 解除线索和活动的关联，即根据ID删除关系表中的记录
     * @param request  请求对象
     * @param response 响应对象
     */
    private void deleteById(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        ClueActivityRelationService service = (ClueActivityRelationService) UtilOne.getProxyOfCommit(new ClueActivityRelationServiceImpl());
        boolean ok = service.deleteById(id);
        UtilOne.printBoolean(response,ok);
    }

    /**
     * 获取已经和某条线索关联的市场活动
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getguanlianActivityList(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        List<Activity> list = service.getguanlianActivityList(clueId);
        UtilOne.printJson(response,list);
    }

    /**
     * 保存市场活动和线索的关联
     * @param request  请求对象
     * @param response 响应对象
     */
    private void saveguanlian(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        String[] ids = request.getParameterValues("id");
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("activityIds",ids);
        ClueActivityRelationService service = (ClueActivityRelationService) UtilOne.getProxyOfCommit(new ClueActivityRelationServiceImpl());
        boolean ok = service.saveguanlian(map);
        UtilOne.printBoolean(response,ok);
    }

    /**
     * 查询没有和某条线索关联的所有市场活动
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        String clueId = request.getParameter("clueId");
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        List<Activity> list = service.getAllActivityList(clueId);
        UtilOne.printJson(response,list);
    }

    /**
     * 根据ID查线索，单条,转发到jsp
     * @param request  请求对象
     * @param response 响应对象
     */
    private void selectById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        ClueService service = (ClueService) UtilOne.getProxyOfCommit(new ClueServiceImpl());
        Clue clue = service.selectById(id);
        request.getSession().setAttribute("clue",clue);
        request.getRequestDispatcher("/work/clue/detail.jsp").forward(request,response);
    }

    /**
     * 获取满足条件的线索列表，动态查询
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getClueList(HttpServletRequest request, HttpServletResponse response) {
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");
        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        pageNo = (pageNo-1)*pageSize;
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);
        ClueService service = (ClueService) UtilOne.getProxyOfCommit(new ClueServiceImpl());
        PageVo vo = service.getClueList(map);
        UtilOne.printJson(response,vo);
    }
    /**
     * 保存线索
     * @param request  请求对象
     * @param response 响应对象
     */
    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        String id = UtilOne.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String createTime = UtilOne.getTime();
        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        ClueService service = (ClueService) UtilOne.getProxyOfCommit(new ClueServiceImpl());
        boolean ok = service.saveClue(clue);
        UtilOne.printBoolean(response,ok);
    }

    /**
     * 获取user列表
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService service = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        List<User> userList = service.getUserList();
        UtilOne.printJson(response, userList);
    }
}
