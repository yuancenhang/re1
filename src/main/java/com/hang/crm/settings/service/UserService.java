package com.hang.crm.settings.service;

import com.hang.crm.settings.domain.User;
import com.hang.crm.exception.loginException;

import java.util.List;

public interface UserService {
    User login(String username,String password,String ip) throws loginException;

    List<User> getUserList();
}
