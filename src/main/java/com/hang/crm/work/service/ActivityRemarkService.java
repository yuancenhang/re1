package com.hang.crm.work.service;

import com.hang.crm.work.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> loadRemark(String activityId);
}
