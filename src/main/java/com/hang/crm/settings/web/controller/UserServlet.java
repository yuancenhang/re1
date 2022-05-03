package com.hang.crm.settings.web.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.exception.loginException;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)  {
        if ("/crm/User/login.sv".equals(req.getRequestURI())) {
            login(req,resp);
        }
    }

    private void login(HttpServletRequest request,HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ip = request.getRemoteAddr();
        System.out.println("密码是"+password);
        password = UtilOne.getMD5(password);
        System.out.println("密码是"+password);
        UserService userService = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        try {
            System.out.println("进入try");
            User user = userService.login(username,password,ip);
            request.getSession().setAttribute("user",user);
            UtilOne.printBoolean(response,true);
            System.out.println(user);
        } catch (loginException e) {
            e.printStackTrace();
            Map<String,Object> map = new HashMap<>();
            map.put("ok",false);
            map.put("msg",e.getMessage());
            UtilOne.printJson(response,map);
        }

    }

}
