package com.hang.crm.work.service;

import com.hang.crm.work.domain.Tran;

import java.util.List;

public interface CustomerService {
    List<String> getCustomerListByName(String name);
}
