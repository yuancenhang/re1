package com.hang.crm.work.service;

import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    Integer activitySave(Activity activity);

    PageVo getActivityList(Map<String, Object> map);

    boolean deleteActivity(String[] ids);

    Map<String, Object> editActivity(String id);

    boolean editSave(Map<String, Object> map);

    Activity loadDetail(String activityId);

    List<Activity> getAllActivityList();
}
