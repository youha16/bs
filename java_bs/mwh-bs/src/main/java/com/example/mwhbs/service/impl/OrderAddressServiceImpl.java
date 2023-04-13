package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.OrderAddressMapper;
import com.example.mwhbs.entity.OrderAddress;
import com.example.mwhbs.service.OrderAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderAddressServiceImpl implements OrderAddressService {

    @Resource
    private OrderAddressMapper orderAddressMapper;

    /**
     * 为订单新增地址信息
     * @param orderAddress
     * @return
     */
    public boolean addOrderAddress(OrderAddress orderAddress){
        return orderAddressMapper.insert(orderAddress)==1;
    }

    /**
     * 更新订单的地址信息，未验证用户身份
     * @param orderAddress
     * @return
     */
    public boolean updateOrderAddress(OrderAddress orderAddress){
        orderAddress.setOrderId(null);
        return orderAddressMapper.updateByPrimaryKeySelective(orderAddress)==1;
    }

    /**
     * 获取订单的地址信息
     * orderId建索引
     * @param orderId
     * @return
     */
    public OrderAddress getOrderAddress(Long orderId){
        return orderAddressMapper.selectByOrderId(orderId);
    }
}
