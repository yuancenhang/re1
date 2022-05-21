package com.hang.crm.work.dao;

import com.hang.crm.work.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int saveClue(Clue clue);

    int selectCount(Map<String, Object> map);

    List<Clue> getClueList(Map<String, Object> map);

    Clue selectById(String id);
}
