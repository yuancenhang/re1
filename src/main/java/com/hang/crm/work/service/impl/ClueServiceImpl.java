package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.*;
import com.hang.crm.work.domain.*;
import com.hang.crm.work.service.ClueService;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao = UtilOne.getSqlSession().getMapper(ClueDao.class);
    CustomerDao customerDao = UtilOne.getSqlSession().getMapper(CustomerDao.class);
    ContactsDao contactsDao = UtilOne.getSqlSession().getMapper(ContactsDao.class);
    ClueRemarkDao clueRemarkDao = UtilOne.getSqlSession().getMapper(ClueRemarkDao.class);
    ContactsRemarkDao contactsRemarkDao = UtilOne.getSqlSession().getMapper(ContactsRemarkDao.class);
    CustomerRemarkDao customerRemarkDao = UtilOne.getSqlSession().getMapper(CustomerRemarkDao.class);
    ClueActivityRelationDao clueActivityRelationDao = UtilOne.getSqlSession().getMapper(ClueActivityRelationDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao = UtilOne.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    TranDao tranDao = UtilOne.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = UtilOne.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean saveClue(Clue clue) {
        int count = clueDao.saveClue(clue);
        return count==1;
    }

    @Override
    public PageVo getClueList(Map<String, Object> map) {
        PageVo<Clue> vo = new PageVo<>();
        int total = clueDao.selectCount(map);
        vo.setTotal(total);
        List<Clue> list = clueDao.getClueList(map);
        vo.setList(list);
        return vo;
    }

    @Override
    public Clue selectById(String id) {
        return clueDao.selectById(id);
    }

    /**
     * 转换线索成客户和联系人
     * @param clueId 线索ID
     * @param tran 交易对象
     * @param createBy 创建人
     * @return 全部成功返回true
     */
    @Override
    public boolean convert(String clueId, Tran tran, String createBy) {
        boolean flag = true;
        //获取线索对象
        Clue clue = clueDao.selectById(clueId);
        String createTime = UtilOne.getTime();
        //通过线索获取客户（company）如果没有新建一个
        String company = clue.getCompany();
        Customer customer = customerDao.selectByName(company);
        if (customer==null){
            customer = new Customer();
            customer.setId(UtilOne.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());
            int count = customerDao.save(customer);
            if (count!=1) flag = false;
        }
        //创建联系人
        Contacts contacts = new Contacts();
        contacts.setId(UtilOne.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        int count = contactsDao.save(contacts);
        if (count!=1) flag=false;
        //把线索的备注转移到客户和联系人,3个备注的属性都是一样的
        List<ClueRemark> list = clueRemarkDao.selectByClueId(clueId);
        if (list!=null){
            for(ClueRemark clueRemark : list){
                //备注给联系人
                String noteContent = clueRemark.getNoteContent();
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UtilOne.getUUID());
                contactsRemark.setNoteContent(noteContent);
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag("0");
                contactsRemark.setContactsId(contacts.getId());
                count = contactsRemarkDao.save(contactsRemark);
                if (count!=1) flag=false;
                //备注给客户
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UtilOne.getUUID());
                customerRemark.setNoteContent(noteContent);
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag("0");
                customerRemark.setCustomerId(contacts.getId());
                count = customerRemarkDao.save(customerRemark);
                if (count!=1) flag=false;
            }
        }
        //线索和活动的关系转换到联系人和活动的关系
        List<ClueActivityRelation> list1 = clueActivityRelationDao.getListByClueId(clueId);
        if (list1!=null){
            for (ClueActivityRelation clueActivityRelation : list1){
                String activityId = clueActivityRelation.getActivityId();
                String contactsId = contacts.getId();
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UtilOne.getUUID());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(contactsId);
                count = contactsActivityRelationDao.save(contactsActivityRelation);
                if (count!=1) flag=false;
            }
        }
        //如果要创建交易
        if (tran!=null){
            //保存交易
            count = tranDao.save(tran);
            if (count!=1) flag=false;
            //保存交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UtilOne.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());
            count = tranHistoryDao.save(tranHistory);
            if (count!=1) flag=false;
        }

        //删除线索备注
        if (list!=null) {
            for (ClueRemark clueRemark : list) {
                count = clueRemarkDao.deleteById(clueRemark.getId());
                if (count != 1) flag = false;
            }
        }

        //删除线索和活动的关系
        if (list1!=null) {
            for (ClueActivityRelation clueActivityRelation : list1) {
                count = clueActivityRelationDao.deleteById(clueActivityRelation.getId());
                if (count != 1) flag = false;
            }
        }

        //删除线索
        count = clueDao.deleteById(clueId);
        if (count!=1) flag=false;

        return flag;
    }
}
