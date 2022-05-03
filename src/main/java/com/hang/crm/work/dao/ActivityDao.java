package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.vo.PageVo;

import java.util.List;

public interface ActivityDao {

    Integer activitySave(Activity activity);

    List<Activity> getActivityList();
}
