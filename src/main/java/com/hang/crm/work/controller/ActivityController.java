package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/activity/getUserList.sv".equals(path)){
            getUserList(request,response);
        }else if ("/work/activity/save.sv".equals(path)){
            avtivitySave(request,response);
        }
    }

    private void avtivitySave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进去save");
        //取数据塞进activity对象
        String id = UtilOne.getUUID();
        String owner =request.getParameter("owner");
        String name =request.getParameter("name");
        String startDate =request.getParameter("startDate");
        String endDate =request.getParameter("endDate");
        String cost =request.getParameter("cost");
        String description =request.getParameter("description");
        String createTime = UtilOne.getTime();
        String createBy = ((User)(request.getSession().getAttribute("user"))).getName();
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
        System.out.println(i);
        UtilOne.printBoolean(response, i == 1);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        List<User> ulist = us.getUserList();
        System.out.println(ulist);
        UtilOne.printJson(response,ulist);
    }
}
