package com.hang.crm.work.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/work/clue/xxx.sv".equals(path)){
            //getUserList(request,response);
        }else if ("/work/clue/xxxx.sv".equals(path)){
            //activitySave(request,response);
        }
    }
}
