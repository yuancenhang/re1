package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Customer;
import com.hang.crm.work.domain.Tran;

import java.util.List;

public interface CustomerDao {

    Customer selectByName(String company);

    int save(Customer customer);

    List<String> getCustomerListByName(String name);

    List<String> getIdsByName(String customerName);

}
