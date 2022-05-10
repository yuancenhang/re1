package com.hang.crm.work.service.impl;

import com.hang.crm.settings.dao.UserDao;
import com.hang.crm.settings.domain.User;
import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ActivityDao;
import com.hang.crm.work.dao.ActivityRemarkDao;
import com.hang.crm.work.domain.Activity;
import com.hang.crm.work.service.ActivityService;
import com.hang.crm.work.vo.PageVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    ActivityDao dao = UtilOne.getSqlSession().getMapper(ActivityDao.class);

    @Override //保存活动
    public Integer activitySave(Activity activity) {
        return dao.activitySave(activity);
    }

    @Override //获取数据，输出到前端
    public Map<String, Object> editActivity(String id) {
        Map<String,Object> map = new HashMap<>();
        //获取userList
        UserDao userDao = UtilOne.getSqlSession().getMapper(UserDao.class);
        List<User> userList = userDao.getUserList();
        map.put("userList",userList);
        System.out.println("u========"+userList);
        //获取活动信息
        Activity activity = dao.selectById(id);
        map.put("activityName",activity.getName());
        map.put("startDate",activity.getStartDate());
        map.put("endDate",activity.getEndDate());
        map.put("cost",activity.getCost());
        map.put("description",activity.getDescription());
        return map;
    }

    @Override //把前端来的数据更新到数据库t_activity表
    public boolean editSave(Map<String, Object> map) {
        int count = dao.editSave(map);
        return count == 1;
    }

    @Override //删除活动，设计t_activity表，t_activityRemark备注表
    public boolean deleteActivity(String[] ids) {
        boolean flag = true;
        ActivityRemarkDao remarkDao = UtilOne.getSqlSession().getMapper(ActivityRemarkDao.class);
        int count1 = remarkDao.getCount(ids); //查备注条数
        int count2 = remarkDao.delete(ids); //返回删除的备注条数
        int count3 = dao.delete(ids); //删除的活动条数
        if (count1 != count2 || count3 != ids.length){
            flag = false;
        }
        return flag;
    }

    @Override //获取用户列表，user表，不是activity表
    public PageVo getActivityList(Map<String, Object> map) {
        List<Activity> alist = dao.getActivityList(map);
        Integer total = dao.selectTotal(map);
        PageVo<Activity> pageVo = new PageVo<>();
        pageVo.setList(alist);
        pageVo.setTotal(total);
        return pageVo;
    }
}
