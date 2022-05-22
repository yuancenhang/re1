package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ClueActivityRelationDao;
import com.hang.crm.work.service.ClueActivityRelationService;

import java.util.HashMap;
import java.util.Map;

public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    ClueActivityRelationDao relationDao = UtilOne.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public boolean saveguanlian(Map<String, Object> map) {
        String[] ids = (String[]) map.get("activityIds");
        int count = 0;
        Map<String,Object> map1 = new HashMap<>();
        for (String id : ids) {
            map1.put("id",UtilOne.getUUID());
            map1.put("clueId",  map.get("clueId"));
            map1.put("activityId",id);
            count += relationDao.saveguanlian(map1);
        }
        return ids.length==count;
    }

    @Override
    public boolean deleteById(String id) {
        int count = 1;
        return count==relationDao.deleteById(id);
    }
}
