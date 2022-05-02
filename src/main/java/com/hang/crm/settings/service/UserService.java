package com.hang.crm.settings.service;

import com.hang.crm.settings.domain.User;
import com.hang.crm.exception.loginException;

public interface UserService {
    User login(String username,String password,String ip) throws loginException;
}
