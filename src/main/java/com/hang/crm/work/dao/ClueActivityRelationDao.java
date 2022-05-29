package com.hang.crm.work.dao;

import com.hang.crm.work.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    int saveguanlian(Map<String, Object> map);

    int deleteById(String id);

    List<ClueActivityRelation> getListByClueId(String clueId);

}
