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

    @Override
    public ActivityRemark getRemark(String rid) {
        return remarkDao.getRemarkById(rid);
    }

    @Override
    public boolean updateRemark(ActivityRemark remark) {
        int count = remarkDao.updateById(remark);
        return count == 1 ;
    }

    @Override
    public boolean saveRemark(ActivityRemark remark) {
        int count = remarkDao.insert(remark);
        return count == 1 ;
    }

    @Override
    public boolean deleteRemark(String rid) {
        int count = remarkDao.deleteById(rid);
        return count == 1 ;
    }
}
