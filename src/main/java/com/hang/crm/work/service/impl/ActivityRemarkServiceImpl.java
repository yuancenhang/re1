package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ActivityRemarkDao;
import com.hang.crm.work.domain.ActivityRemark;
import com.hang.crm.work.service.ActivityRemarkService;

import java.util.List;

public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    ActivityRemarkDao remarkDao = UtilOne.getSqlSession().getMapper(ActivityRemarkDao.class);

    @Override
    public List<ActivityRemark> loadRemark(String activityId) {
        return remarkDao.selectByActivityId(activityId);
    }
}
