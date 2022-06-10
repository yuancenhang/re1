package com.hang.crm.work.dao;

import com.hang.crm.work.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getTranHistoryListByTranId(String tranId);
}
