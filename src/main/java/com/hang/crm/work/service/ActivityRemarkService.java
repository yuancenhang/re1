package com.hang.crm.work.service;

import com.hang.crm.work.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> loadRemark(String activityId);

    ActivityRemark getRemark(String rid);

    boolean updateRemark(ActivityRemark remark);

    boolean saveRemark(ActivityRemark remark);

    boolean deleteRemark(String rid);
}
