package com.hang.crm.settings.service.impl;

import com.hang.crm.settings.dao.DicDao;
import com.hang.crm.settings.domain.DicType;
import com.hang.crm.settings.domain.DicValue;
import com.hang.crm.settings.service.DicService;
import com.hang.crm.utils.UtilOne;

import javax.servlet.ServletContext;
import java.util.List;

public class DicServiceImpl implements DicService {
    DicDao dao = UtilOne.getSqlSession().getMapper(DicDao.class);

    /**
     * 把数据存进全局作用域
     * @param application 全局作用域
     */
    @Override
    public void write(ServletContext application) {
        List<DicType> typeList = dao.getDicTypeList();
        for (DicType dicType : typeList) {
            String type = dicType.getCode();
            List<DicValue> valueList = dao.getDicValueList(type);
            application.setAttribute(type+"List",valueList);
        }
    }
}
