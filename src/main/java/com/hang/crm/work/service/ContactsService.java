package com.hang.crm.work.service;

import com.hang.crm.work.domain.Contacts;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    PageVo<Contacts> getContactsListByName(Map map);
}
