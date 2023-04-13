package com.example.mwhbs.service;

import com.example.mwhbs.entity.Order;
import com.example.mwhbs.vo.PageVo;

import java.util.List;

public interface OrderService {

    /**
     * 新增订单
     * @param order
     * @return
     */
    boolean addOrder(Order order);

    /**
     * 获取订单信息
     * @param id
     * @return
     */
    Order getOrder(Long id);

    /**
     * 更新订单信息
     * @param order
     * @return
     */
    boolean updateOrder(Order order);

    /**
     * 获取某个用户买到的闲置的订单列表
     * @param userId
     * @return
     */
    List<Order> getMyOrder(Long userId);

    /**
     * 获取某个用户卖出的闲置的订单信息
     * @param userId
     * @return
     */
    List<Order> getMySoldIdle(Long userId);

    PageVo<Order> getAllOrder(int page, int nums);

    boolean deleteOrder(long id);
}
