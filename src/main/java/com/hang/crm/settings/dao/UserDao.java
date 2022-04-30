package com.hang.crm.settings.dao;

import com.hang.crm.settings.domain.User;

import java.util.Map;

public interface UserDao {

    User login(String username);
}
