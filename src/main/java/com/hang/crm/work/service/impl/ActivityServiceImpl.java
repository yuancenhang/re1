package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ActivityDao;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.service.ActivityService;

public class ActivityServiceImpl implements ActivityService {
    @Override
    public Integer activitySave(Activity activity) {
        ActivityDao ad = UtilOne.getSqlSession().getMapper(ActivityDao.class);
        return ad.activitySave(activity);
    }
}
