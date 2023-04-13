package com.example.mwhbs.service;

import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.vo.PageVo;

import java.util.List;

public interface IdleItemService {

    /**
     * 发布闲置
     * @param idleItem
     * @return
     */
    boolean addIdleItem(IdleItem idleItem);

    /**
     * 获取某个闲置的信息
     * @param id
     * @return
     */
    IdleItem getIdleItem(Long id);

    /**
     * 获取某个用户的所有闲置信息
     * @param userId
     * @return
     */
    List<IdleItem> getAllIdelItem(Long userId);

    /**
     * 搜索闲置
     * @param findValue
     * @param page
     * @param nums
     * @return
     */
    PageVo<IdleItem> findIdleItem(String findValue, int page, int nums);

    /**
     * 按分类获取闲置，分页器
     * @param idleLabel
     * @param page
     * @param nums
     * @return
     */
    PageVo<IdleItem> findIdleItemByLable(int idleLabel, int page, int nums);

    /**
     * 更新闲置的状态信息
     * @param idleItem
     * @return
     */
    boolean updateIdleItem(IdleItem idleItem);

    PageVo<IdleItem> adminGetIdleList(int status, int page, int nums) ;
}
