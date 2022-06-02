package com.hang.crm.work.controller;

import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.settings.service.impl.UserServiceImpl;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.service.CustomerService;
import com.hang.crm.work.service.TranService;
import com.hang.crm.work.service.impl.CustomerServiceImpl;
import com.hang.crm.work.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/transaction/loadData.sv".equals(path)) {
            getUserListAndOther(request, response);
        } else if ("/work/transaction/getCustomerList.sv".equals(path)) {
            getCustomerList(request, response);
        }
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
