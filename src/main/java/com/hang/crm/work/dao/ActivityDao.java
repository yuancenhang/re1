package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ActivityDao {

    Integer activitySave(Activity activity);

    List<Activity> getActivityList(Map map);

    Integer selectTotal(Map map);

    int delete(String[] ids);

    Activity selectById(String id);

    int editSave(Map<String, Object> map);
}
