package com.hang.crm.listener;

import com.hang.crm.settings.service.DicService;
import com.hang.crm.settings.service.impl.DicServiceImpl;
import com.hang.crm.utils.UtilOne;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 监听器，监听全局作用域ServletContext的创建
 */
public class ApplicationListener implements ServletContextListener {
    /*
    在服务器启动时会创建全局作用域，在这时候从数据库获取数据字典，存进作用域中。
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DicService service = (DicService) UtilOne.getProxyOfCommit(new DicServiceImpl());
        ServletContext application = sce.getServletContext();
        service.write(application);
    }
}
