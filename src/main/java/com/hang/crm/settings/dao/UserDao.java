package com.hang.crm.settings.dao;

import com.hang.crm.settings.domain.User;

import java.util.List;

public interface UserDao {

    User login(String username);

    List<User> getUserList();
}
