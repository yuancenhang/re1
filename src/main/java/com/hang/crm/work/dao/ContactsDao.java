package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int save(Contacts contacts);

    List<String> getIdsByName(String contactsName);

    List<Contacts> getContactsListByName(Map map);

    int getContactsCountByName(Map map);
}
