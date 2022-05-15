package com.hang.crm.settings.dao;

import com.hang.crm.settings.domain.DicType;
import com.hang.crm.settings.domain.DicValue;

import java.util.List;

public interface DicDao {

    List<DicType> getDicTypeList();

    List<DicValue> getDicValueList(String type);
}
