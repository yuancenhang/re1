package com.hang.crm.work.dao;

import com.hang.crm.work.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> selectByClueId(String clueId);

    int deleteById(String clueId);
}
