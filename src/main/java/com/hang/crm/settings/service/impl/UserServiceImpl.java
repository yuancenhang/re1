package com.hang.crm.settings.service.impl;

import com.hang.crm.settings.dao.UserDao;
import com.hang.crm.settings.domain.User;
import com.hang.crm.settings.exception.loginException;
import com.hang.crm.settings.service.UserService;
import com.hang.crm.utils.UtilOne;

public class UserServiceImpl implements UserService {
    UserDao userDao = UtilOne.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String username, String password,String ip) throws loginException{
        User user = userDao.login(username);
        if (null == user) throw new loginException("用户不存在");
        if (user.getLoginPwd() == null) throw new loginException("密码错误");
        if (!(user.getAllowIps().equals(ip))) throw new loginException("ip地址不允许登陆");
        return user;
    }
}
