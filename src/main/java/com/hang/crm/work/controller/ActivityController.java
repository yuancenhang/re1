package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;

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
        }
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        List<User> ulist = us.getUserList();
        System.out.println(ulist);
        UtilOne.printJson(response,ulist);
    }
}
