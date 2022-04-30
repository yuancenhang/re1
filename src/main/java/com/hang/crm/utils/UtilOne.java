package com.hang.crm.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UtilOne {
    private static SqlSessionFactory factory = null;
    private static ThreadLocal<SqlSession> t = new ThreadLocal<>();

    static{
        try {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            factory = new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个SqlSession
     * @return SqlSession
     */
    public static SqlSession getSqlSession(){
        SqlSession session = t.get();
        if (session == null){
            session = factory.openSession();
            t.set(session);
        }
        return session;
    }

    /**
     * 关闭一个SqlSession
     * @param session 传入需要关闭的SqlSession对象
     */
    public static void closeSqlSession(SqlSession session){
        if (session != null) {
            session.close();
            t.remove();
        }
    }

    /**
     * 把对象转换成json串
     * @param o 需要转换的对象
     * @return 返回json串
     */
    public static String toJson(Object o){
        String json = "";
        try {
            json = new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 传入对象，获取代理对象，作用是完成事务提交
     * @param o 需要创建代理的对象
     * @return 传入对象的代理对象
     */
    public static Object getProxyOfCommit(Object o){
        CommitHandler handler = new CommitHandler(o);
        return handler.proxy();
    }

    /**
     * 获取UUID，全球唯一标识字符串
     * @return 返回唯一的字符串
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 获取当前时间的19为时间字符串
     * @return 返回当前时间的19位字符串
     */
    public static String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 把密码加密
     * @param password 传入需要加密的字符串
     * @return 返回加密后的字符串
     */
    public static String getMD5(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] result = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result){
                int number = b & 0xff;
                String s = Integer.toHexString(number);
                if (s.length() == 1){
                    sb.append("0");
                }
                sb.append(s);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 输出一个boolean到浏览器
     * @param response 传入response
     */
    public static void printFalse(HttpServletResponse response,boolean b){
        Map<String,Object> map = new HashMap<>();
        map.put("ok",b);
        String json = toJson(map);
        print(response,json);
    }

    /**
     * 把实体对象转成json并输出到浏览器
     * @param response 响应对象
     * @param obj 需要转成json的对象
     */
    public static void printJson(HttpServletResponse response,Object obj){
        String json = toJson(obj);
        print(response,json);
    }

    /**
     * 封装好的输出json到浏览器的方法
     * @param response 响应对象
     * @param o 输出的内容
     */
    private static void print(HttpServletResponse response,Object o){
        try {
            response.getWriter().print(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}





























