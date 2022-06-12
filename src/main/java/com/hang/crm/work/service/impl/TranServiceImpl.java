package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ContactsDao;
import com.hang.crm.work.dao.CustomerDao;
import com.hang.crm.work.dao.TranDao;
import com.hang.crm.work.dao.TranHistoryDao;
import com.hang.crm.work.domain.Customer;
import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.domain.TranHistory;
import com.hang.crm.work.service.TranService;
import com.hang.crm.work.vo.PageVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    CustomerDao customerDao = UtilOne.getSqlSession().getMapper(CustomerDao.class);
    ContactsDao contactsDao = UtilOne.getSqlSession().getMapper(ContactsDao.class);
    TranDao tranDao = UtilOne.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao = UtilOne.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public PageVo<Tran> getTranListDT(Map<String, Object> map) {
        //先查出所有名字像customerName的ID
        String customerName = (String) map.get("customerName");
        List<String> customerIdList = customerDao.getIdsByName(customerName);

        //再查出所有名字像contactsName的ID
        String contactsName = (String) map.get("contactsName");
        List<String> contactsIdList = contactsDao.getIdsByName(contactsName);

        if (customerIdList != null) map.put("customerIds",customerIdList);
        if (contactsIdList != null) map.put("contactsIds",contactsIdList);

        List<Tran> list = tranDao.getTranListDT(map);
        PageVo<Tran> pageVo = new PageVo<>();
        pageVo.setList(list);
        int total = tranDao.getTranListTotalDT(map);
        pageVo.setTotal(total);
        return pageVo;
    }

    @Override
    public boolean saveTran(Tran tran) {
        boolean ok = true;
        Customer customer = customerDao.selectByName(tran.getCustomerId());
        if (customer == null){  //如果客户不存在，新建，保存
            customer = new Customer();
            customer.setId(UtilOne.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(tran.getCustomerId());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());
            if (1 != customerDao.save(customer)) ok = false;
        }
        tran.setCustomerId(customer.getId());
        if (1 != tranDao.saveTran(tran)) ok = false;
        //每次保存交易都创建一条交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UtilOne.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        if (1 != tranHistoryDao.save(tranHistory)) ok = false;
        return ok;
    }

    @Override
    public Tran getTranById(String id) {
        return tranDao.getTranById(id);
    }

    @Override
    public List<TranHistory> getTranHistoryListByTranId(String tranId) {
        return tranHistoryDao.getTranHistoryListByTranId(tranId);
    }

    @Override
    public boolean changeStage(TranHistory tranHistory) {
        boolean ok = true;
        Tran tran = new Tran();
        tran.setId(tranHistory.getTranId());
        tran.setStage(tranHistory.getStage());
        tran.setEditBy(tranHistory.getCreateBy());
        tran.setEditTime(tranHistory.getCreateTime());
        if (1!=tranDao.updateStage(tran)) ok = false;
        if (1!=tranHistoryDao.save(tranHistory)) ok = false;
        return ok;
    }

    @Override
    public Map<String, Object> tranEcharts() {
        int total = tranDao.getTotal();
        List<Map<String,Object>> list = tranDao.echarts();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("list",list);
        return map;
    }
}
