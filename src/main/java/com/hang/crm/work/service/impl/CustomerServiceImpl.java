package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.CustomerDao;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.service.CustomerService;
import com.hang.crm.work.service.TranService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao = UtilOne.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerListByName(String name) {
        return customerDao.getCustomerListByName(name);
    }
}
