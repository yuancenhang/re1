package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Tran;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    List<Tran> getTranListDT(Map<String, Object> map);

    int getTranListTotalDT(Map<String, Object> map);

    int saveTran(Tran tran);

    Tran getTranById(String id);

}
