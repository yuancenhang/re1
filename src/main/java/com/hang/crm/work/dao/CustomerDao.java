package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Customer;

public interface CustomerDao {

    Customer selectByName(String company);

    int save(Customer customer);
}
