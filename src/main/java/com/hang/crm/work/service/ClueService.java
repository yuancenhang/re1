package com.hang.crm.work.service;

import com.hang.crm.work.domain.Clue;
import com.hang.crm.work.vo.PageVo;

import java.util.Map;

public interface ClueService {
    boolean saveClue(Clue clue);

    PageVo getClueList(Map<String, Object> map);

    Clue selectById(String id);
}
