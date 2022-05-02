package com.hang.crm.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 过滤恶意登陆
 */
public class SafeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getServletPath();
        //如果访问的是登陆相关，放行
        HttpSession session = request.getSession();
        if (null == session.getAttribute("user")) {
            System.out.println("user为null");
            if ("/User/login.sv".equals(path) || "/login.jsp".equals(path) ||"/index.html".equals(path)) {
                System.out.println("登陆，不管");
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            System.out.println("非法访问");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
        System.out.println("放行");
        filterChain.doFilter(servletRequest, servletResponse);

        /*System.out.println(request.getServletPath());  // /login.jsp
        System.out.println(request.getRequestURI());  // /crm/login.jsp
        System.out.println(request.getContextPath());  // /crm
        System.out.println(request.getHttpServletMapping());  // org.apache.catalina.core.ApplicationMapping$MappingImpl@3946a150
        System.out.println(request.getRequestURL());  // http://localhost:8080/crm/login.jsp
        System.out.println(request.getServerName());  // localhost
        System.out.println(request.getServletContext());  // org.apache.catalina.core.ApplicationContextFacade@6a24cc1b
        System.out.println(request.getServerPort());  // 8080*/
    }
}
