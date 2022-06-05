package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ContactsDao;
import com.hang.crm.work.domain.Contacts;
import com.hang.crm.work.service.ContactsService;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public class ContactsServiceImpl implements ContactsService {
    ContactsDao contactsDao = UtilOne.getSqlSession().getMapper(ContactsDao.class);

    @Override
    public PageVo<Contacts> getContactsListByName(Map map) {
        List<Contacts> list = contactsDao.getContactsListByName(map);
        System.out.println("=================================="+list);
        int total = contactsDao.getContactsCountByName(map);
        System.out.println("=================================="+total);
        PageVo vo = new PageVo();
        vo.setList(list);
        vo.setTotal(total);
        return vo;
    }
}
