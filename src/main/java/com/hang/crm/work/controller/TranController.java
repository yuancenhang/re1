package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.domain.Contacts;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.domain.TranHistory;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.service.ContactsService;
import com.hang.crm.work.service.CustomerService;
import com.hang.crm.work.service.TranService;
import com.hang.crm.work.service.impl.ActivityServiceImpl;
import com.hang.crm.work.service.impl.ContactsServiceImpl;
import com.hang.crm.work.service.impl.CustomerServiceImpl;
import com.hang.crm.work.service.impl.TranServiceImpl;
import com.hang.crm.work.vo.PageVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/transaction/loadData.sv".equals(path)) {
            getUserListAndOther(request, response);
        }else if ("/work/transaction/getCustomerList.sv".equals(path)) {
            getCustomerList(request, response);
        }else if ("/work/transaction/loadTranList.sv".equals(path)) {
            getTranListDT(request, response);
        }else if ("/work/transaction/getActivityListByName.sv".equals(path)) {
            getActivityListByName(request, response);
        }else if ("/work/transaction/getContactsListByName.sv".equals(path)) {
            getContactsListByName(request, response);
        }else if ("/work/transaction/saveTran.sv".equals(path)) {
            saveTran(request, response);
        }else if ("/work/transaction/detail.sv".equals(path)) {
            getTranById(request, response);
        }else if ("/work/transaction/loadTranHistoryList.sv".equals(path)) {
            getTranHistoryListByTranId(request, response);
        }
    }

    /*
    通过交易id获取交易历史，展示列表
     */
    private void getTranHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
        String tranId = request.getParameter("tranId");
        TranService service = (TranService) UtilOne.getProxyOfCommit(new TranServiceImpl());
        List<TranHistory> list = service.getTranHistoryListByTranId(tranId);
        Map<String,String> map = (Map<String, String>) this.getServletContext().getAttribute("stage");
        for (TranHistory t : list){
            String possibility = map.get(t.getStage());
            t.setPossibility(possibility);
        }
        UtilOne.printJson(response,list);
    }


    /*
    通过id获取交易信息，跳转详细信息页面
     */
    private void getTranById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        TranService service = (TranService) UtilOne.getProxyOfCommit(new TranServiceImpl());
        Tran tran = service.getTranById(id);
        request.setAttribute("tran",tran);
        //获取可能性
        Map<String,String> map = (Map<String, String>) this.getServletContext().getAttribute("stage");
        String stage = tran.getStage();
        String possibility = map.get(stage);
        request.setAttribute("possibility",possibility);
        request.getRequestDispatcher("/work/transaction/detail.jsp").forward(request,response);
    }

    /*
    保存交易
     */
    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = UtilOne.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = UtilOne.getTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        String customerName = request.getParameter("customerName");

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        tran.setCustomerId(customerName);  //暂时把名字装进id里传进去

        TranService service = (TranService) UtilOne.getProxyOfCommit(new TranServiceImpl());
        boolean ok = service.saveTran(tran);
        if (ok) response.sendRedirect(request.getContextPath() + "/work/transaction/index.jsp");
    }

    /*
    根据名字查找联系人，返回VO
     */
    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {
        String fullname = request.getParameter("fullname");
        String no = request.getParameter("pageNo");
        String size = request.getParameter("pageSize");
        int pageNo = Integer.parseInt(no);
        int pageSize = Integer.parseInt(size);
        pageNo = (pageNo-1)*pageSize;
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        ContactsService service = (ContactsService) UtilOne.getProxyOfCommit(new ContactsServiceImpl());
        PageVo<Contacts> vo = service.getContactsListByName(map);
        UtilOne.printJson(response,vo);
    }

    /*
    获取市场活动列表，名字模糊查询，
     */
    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String no = request.getParameter("pageNo");
        String size = request.getParameter("pageSize");
        int pageNo = Integer.parseInt(no);
        int pageSize = Integer.parseInt(size);
        pageNo = (pageNo-1)*pageSize;

        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        PageVo<Activity> vo = service.getActivityList(map);
        UtilOne.printJson(response,vo);
    }

    /*
    根据查询条件，动态查询满足条件的交易tran
     */
    private void getTranListDT(HttpServletRequest request, HttpServletResponse response) {
        String no = request.getParameter("pageNo");
        String size = request.getParameter("pageSize");
        int pageNo = Integer.parseInt(no);
        int pageSize = Integer.parseInt(size);
        pageNo = (pageNo-1)*pageSize;

        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");
        //最终查询
        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerName",customerName);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsName",contactsName);

        TranService tranService = (TranService) UtilOne.getProxyOfCommit(new TranServiceImpl());
        PageVo<Tran> pageVo = tranService.getTranListDT(map);
        UtilOne.printJson(response,pageVo);
    }

    /*
    获取客户列表，模糊查询，
     */
    private void getCustomerList(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        CustomerService service = (CustomerService) UtilOne.getProxyOfCommit(new CustomerServiceImpl());
        List<String> list = service.getCustomerListByName(name);
        UtilOne.printJson(response,list);
    }

    /*
    加载创建交易的页面，转发的形式，需要获取所有者列表
     */
    private void getUserListAndOther(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserService userService = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/work/transaction/save.jsp").forward(request,response);
    }
}
