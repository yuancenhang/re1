package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ContactsDao;
import com.hang.crm.work.dao.CustomerDao;
import com.hang.crm.work.dao.TranDao;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.service.TranService;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    CustomerDao customerDao = UtilOne.getSqlSession().getMapper(CustomerDao.class);
    ContactsDao contactsDao = UtilOne.getSqlSession().getMapper(ContactsDao.class);
    TranDao tranDao = UtilOne.getSqlSession().getMapper(TranDao.class);

    @Override
    public PageVo<Tran> getTranListDT(Map<String, Object> map) {
        //先查出所有名字像customerName的ID
        String customerName = (String) map.get("customerName");
        List<String> customerIdList = customerDao.getIdsByName(customerName);

        //再查出所有名字像contactsName的ID
        String contactsName = (String) map.get("contactsName");
        List<String> contactsIdList = contactsDao.getIdsByName(contactsName);

        if (customerIdList != null) map.put("customerIds",customerIdList);
        if (contactsIdList != null) map.put("contactsIds",contactsIdList);

        List<Tran> list = tranDao.getTranListDT(map);
        PageVo<Tran> pageVo = new PageVo<>();
        pageVo.setList(list);
        int total = tranDao.getTranListTotalDT(map);
        pageVo.setTotal(total);
        return pageVo;
    }
}
