package com.hang.crm.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CommitHandler implements InvocationHandler {
    Object target;

    public CommitHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        SqlSession session = null;
        try {
            session = UtilOne.getSqlSession();
            result = method.invoke(target,args);
            session.commit();
        }catch (Exception e){
            assert session != null;
            session.rollback();
            //这个异常不能处理，应该抛出去
            throw e.getCause();
        }
        finally {
            UtilOne.closeSqlSession(session);
        }
        return result;
    }
    public Object proxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }
}
