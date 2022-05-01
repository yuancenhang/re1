package com.hang.crm.settings.web.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.exception.loginException;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)  {
        System.out.println(req.getRequestURI());
        if ("/crm/User/login".equals(req.getRequestURI())) {
            login(req,resp);
        }
    }

    private void login(HttpServletRequest request,HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ip = request.getRemoteAddr();

        System.out.println(ip);

        password = UtilOne.getMD5(password);
        UserService userService = (UserService) UtilOne.getProxyOfCommit(new UserServiceImpl());
        try {
            User user = userService.login(username,password,ip);
            request.getSession().setAttribute("user",user);
            UtilOne.printBoolean(response,true);
        } catch (loginException e) {
            e.printStackTrace();
            Map<String,Object> map = new HashMap<>();
            map.put("ok",false);
            map.put("msg",e.getMessage());
            UtilOne.printJson(response,map);
        }

    }

}
