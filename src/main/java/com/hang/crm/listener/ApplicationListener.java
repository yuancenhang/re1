package com.hang.crm.listener;

import com.hang.crm.settings.service.DicService;
import com.hang.crm.settings.service.impl.DicServiceImpl;
import com.hang.crm.utils.UtilOne;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * 监听器，监听全局作用域ServletContext的创建
 */
public class ApplicationListener implements ServletContextListener {
    /*
    在服务器启动时会创建全局作用域，在这时候
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /*
        从数据库获取数据字典，存进作用域中。
         */
        DicService service = (DicService) UtilOne.getProxyOfCommit(new DicServiceImpl());
        ServletContext application = sce.getServletContext();
        service.write(application);

        /*
        从Stage2Possibility.properties中获取阶段和可能性的对应关系，存进作用域
         */
        Map<String,String> map = new HashMap<>();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Set<String> set = resourceBundle.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String value = resourceBundle.getString(key);
            map.put(key,value);
        }
        application.setAttribute("stage",map);
    }



}
