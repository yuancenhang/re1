package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.service.impl.ActivityServiceImpl;
import com.hang.crm.work.vo.PageVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/activity/getUserList.sv".equals(path)){
            getUserList(request,response);
        }else if ("/work/activity/save.sv".equals(path)){
            avtivitySave(request,response);
        }else if ("/work/activity/pageList.sv".equals(path)){
            getActivityList(request,response);
        }else if ("/work/activity/delete.sv".equals(path)){
            deleteActivity(request,response);
        }else if ("/work/activity/edit.sv".equals(path)){
            editActivity(request,response);
        }else if ("/work/activity/editSave.sv".equals(path)){
            editSave(request,response);
        }
    }

    /**
     * 市场活动修改后保存的方法，update
     * @param request
     * @param response
     */
    private void editSave(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("activityId");
        String owner = request.getParameter("owner");
        String activityName = request.getParameter("activityName");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String editBy = user.getId();
        Map<String,Object> map = new HashMap<>();
        map.put("activityId",activityId);
        map.put("owner",owner);
        map.put("activityName",activityName);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("cost",cost);
        map.put("description",description);
        map.put("editBy",editBy);
        String editTime = UtilOne.getTime();
        map.put("editTime",editTime);

        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        boolean ok = service.editSave(map);
        UtilOne.printBoolean(response,ok);
    }

    /**
     * 修改市场活动的方法
     * @param request
     * @param response
     */
    private void editActivity(HttpServletRequest request, HttpServletResponse response) {
        String activityId = request.getParameter("id");
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        Map<String,Object> map = service.editActivity(activityId);
        UtilOne.printJson(response,map);
    }

    /**
     * 删除市场活动的方法
     * @param request 请求对象
     * @param response 响应对象
     */
    private void deleteActivity(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");
        ActivityService service = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        boolean ok = service.deleteActivity(ids);
        UtilOne.printBoolean(response,ok);
    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo");
        int no = Integer.parseInt(pageNo);

        String pageSize = request.getParameter("pageSize");
        int size = Integer.parseInt(pageSize);
        no = (no-1)*size;

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Map<String,Object> map = new HashMap<>();
        map.put("pageNo",no);
        map.put("pageSize",size);
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);

        ActivityService as = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        PageVo pageVo = as.getActivityList(map);
        UtilOne.printJson(response,pageVo);
    }

    /**
     * 添加市场活动，insert
     * @param request 请求对象
     * @param response 响应对象
     */
    private void avtivitySave(HttpServletRequest request, HttpServletResponse response) {
        //取数据塞进activity对象
        String id = UtilOne.getUUID();
        String owner =request.getParameter("owner");
        String name =request.getParameter("name");
        String startDate =request.getParameter("startDate");
        String endDate =request.getParameter("endDate");
        String cost =request.getParameter("cost");
        String description =request.getParameter("description");
        String createTime = UtilOne.getTime();
        String createBy = ((User)(request.getSession().getAttribute("user"))).getId();
        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        //拿代理，调方法
        ActivityService as = (ActivityService) UtilOne.getProxyOfCommit(new ActivityServiceImpl());
        Integer i = as.activitySave(activity);
        UtilOne.printBoolean(response, i == 1);
    }

    /**
     * 获取用户列表,select
     * @param request 请求对象
     * @param response 响应对象
     */
    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        List<User> ulist = us.getUserList();
        UtilOne.printJson(response,ulist);
    }
}
