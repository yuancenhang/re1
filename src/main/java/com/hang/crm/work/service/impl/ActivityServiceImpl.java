package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ActivityDao;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    ActivityDao ad = UtilOne.getSqlSession().getMapper(ActivityDao.class);
    @Override
    public Integer activitySave(Activity activity) {
        return ad.activitySave(activity);
    }

    @Override
    public PageVo getActivityList(Map<String, Object> map) {
        List<Activity> alist = ad.getActivityList();
        Integer total = alist.size();
        PageVo<Activity> pageVo = new PageVo<>();
        pageVo.setList(alist);
        pageVo.setTotal(total);
        return pageVo;
    }
}
