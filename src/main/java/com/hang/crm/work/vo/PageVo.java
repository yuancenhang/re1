package com.hang.crm.work.vo;

import java.util.List;

/**
 * 一个泛型的vo类，可以把数据库查到的对象的集合和总条数封装进来
 * @param <T> 数据库查到的一类对象的List集合
 */
public class PageVo<T> {
    Integer total;
    List<T> list;

    @Override
    public String toString() {
        return "PageVo{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
