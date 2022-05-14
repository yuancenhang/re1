package com.hang.crm.work.dao;

import com.hang.crm.work.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {

    int getCount(String[] ids);

    int delete(String[] ids);

    List<ActivityRemark> selectByActivityId(String activityId);

    ActivityRemark getRemarkById(String rid);

    int updateById(ActivityRemark remark);

    int insert(ActivityRemark remark);

    int deleteById(String rid);
}
