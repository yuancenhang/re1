package com.hang.crm.work.service;

import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.domain.TranHistory;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface TranService {

    PageVo<Tran> getTranListDT(Map<String, Object> map);

    boolean saveTran(Tran tran);

    Tran getTranById(String id);

    List<TranHistory> getTranHistoryListByTranId(String tranId);

    boolean changeStage(TranHistory tranHistory);

    Map<String, Object> tranEcharts();
}
