package com.hang.crm.work.service.impl;

import com.hang.crm.utils.UtilOne;
import com.hang.crm.work.dao.ClueDao;
import com.hang.crm.work.domain.Clue;
import com.hang.crm.work.service.ClueService;
import com.hang.crm.work.vo.PageVo;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao = UtilOne.getSqlSession().getMapper(ClueDao.class);

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
}
