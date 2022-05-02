package com.hang.crm.settings.service.impl;

import com.hang.crm.settings.dao.UserDao;
import com.hang.crm.settings.domain.User;
import com.hang.crm.exception.loginException;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.utils.UtilOne;

import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao = UtilOne.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String username, String password,String ip) throws loginException{
        User user = userDao.login(username);
        if (null == user) throw new loginException("用户不存在");
        if (!(user.getLoginPwd().equals(password))) throw new loginException("密码错误");
        //if (!(user.getAllowIps().contains(ip))) throw new loginException("ip地址不允许登陆");
        if ("0".equals(user.getLockState())) throw new loginException("账号锁定");
        if (user.getExpireTime().compareTo(UtilOne.getTime()) < 0) throw new loginException("账号过期");
        return user;
    }

    @Override
    public List<User> getUserList() {
        UserDao ud = UtilOne.getSqlSession().getMapper(UserDao.class);
        return ud.getUserList();
    }
}
