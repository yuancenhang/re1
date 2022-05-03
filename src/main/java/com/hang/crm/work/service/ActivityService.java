package com.hang.crm.work.service;

import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.vo.PageVo;

import java.util.Map;

public interface ActivityService {

    Integer activitySave(Activity activity);

    PageVo getActivityList(Map<String, Object> map);
}
