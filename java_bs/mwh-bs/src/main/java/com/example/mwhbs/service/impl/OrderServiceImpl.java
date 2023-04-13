package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.IdleItemMapper;
import com.example.mwhbs.mapper.OrderMapper;
import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.entity.Order;
import com.example.mwhbs.service.OrderService;
import com.example.mwhbs.utils.OrderTask;
import com.example.mwhbs.utils.OrderTaskHandler;
import com.example.mwhbs.vo.PageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private IdleItemMapper idleItemMapper;

    /**
     * 新增订单，同时下架闲置
     * 用了事务串行化，后续要优化，修改更新的sql，增加更新条件，而不是在代码中判断条件
     * 业务逻辑可优化，改为支付时才下架。
     * 新功能待做，需要新增订单超时处理
     * （订单超时：
     * 1、重新上架闲置；2、修改订单状态；
     * 3、确保订单取消前不会影响用户的支付，支付前要判断订单状态并加读锁，取消订单时要判断订单状态为未支付才能取消；
     * 4、保证延期任务一定执行，即确保任务不会因为系统异常而消失）
     * @param orderModel
     * @return
     */

    private static HashMap<Integer,ReentrantLock> lockMap=new HashMap<>();
    static {
//        ReentrantLock lock=new ReentrantLock(true);
        for(int i=0;i<100;i++){
            lockMap.put(i,new ReentrantLock(true));
        }
    }
    public boolean addOrder(Order order){
        IdleItem idleItemModel= idleItemMapper.selectByPrimaryKey(order.getIdleId());
        System.out.println(idleItemModel.getIdleStatus());
        if(idleItemModel.getIdleStatus()!=1){
            return false;
        }
        IdleItem idleItem=new IdleItem();
        idleItem.setId(order.getIdleId());
        idleItem.setUserId(idleItemModel.getUserId());
        idleItem.setIdleStatus((byte)2);

        int key= (int) (order.getIdleId()%100);
        ReentrantLock lock=lockMap.get(key);
        boolean flag;
        try {
            lock.lock();
            flag=addOrderHelp(idleItem, order);
        }finally {
            lock.unlock();
        }
        return flag;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean addOrderHelp(IdleItem idleItem, Order order){
        IdleItem idleItemModel= idleItemMapper.selectByPrimaryKey(order.getIdleId());
        if(idleItemModel.getIdleStatus()!=1){
            return false;
        }
        if(idleItemMapper.updateByPrimaryKeySelective(idleItem)==1){
            if(orderMapper.insert(order)==1){
                order.setOrderStatus((byte) 4);
                //半小时未支付则取消订单
                OrderTaskHandler.addOrder(new OrderTask(order,30*60));
                return true;
            }else {
                new RuntimeException();
            }
        }
        return false;
    }

    /**
     * 获取订单信息，同时获取对应的闲置信息
     * @param id
     * @return
     */
    public Order getOrder(Long id){
        Order order = orderMapper.selectByPrimaryKey(id);
        order.setIdleItem(idleItemMapper.selectByPrimaryKey(order.getIdleId()));
        return order;
    }

    /**
     * 更新订单状态，无验证，后期修改为定制的更新sql
     * 后期改为在支付时下架闲置
     * @param order
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(Order order){
        //不可修改的信息
        order.setOrderNumber(null);
        order.setUserId(null);
        order.setIdleId(null);
        order.setCreateTime(null);
        if(order.getOrderStatus()==4){
            //取消订单,需要优化，减少数据库查询次数
            Order o= orderMapper.selectByPrimaryKey(order.getId());
            if(o.getOrderStatus()!=0){
                return false;
            }
            IdleItem idleItemModel= idleItemMapper.selectByPrimaryKey(o.getIdleId());
            if(idleItemModel.getIdleStatus()==2){
                IdleItem idleItem=new IdleItem();
                idleItem.setId(o.getIdleId());
                idleItem.setUserId(idleItemModel.getUserId());
                idleItem.setIdleStatus((byte)1);
                if(orderMapper.updateByPrimaryKeySelective(order)==1){
                    if(idleItemMapper.updateByPrimaryKeySelective(idleItem)==1){
                        return true;
                    }else {
                        new RuntimeException();
                    }
                }
                return false;
            }else{
                if(orderMapper.updateByPrimaryKeySelective(order)==1){
                    return true;
                }else {
                    new RuntimeException();
                }
            }
        }
        return orderMapper.updateByPrimaryKeySelective(order)==1;
    }

    /**
     * 获取我的所有订单
     * 同时查询出对应的闲置信息，
     * 未做分页
     * userId建索引
     * @param userId
     * @return
     */
    public List<Order> getMyOrder(Long userId){
        List<Order> list= orderMapper.getMyOrder(userId);
        if(list.size()>0){
            List<Long> idleIdList=new ArrayList<>();
            for(Order i:list){
                idleIdList.add(i.getIdleId());
            }
            List<IdleItem> idleItemList = idleItemMapper.findIdleByList(idleIdList);
            Map<Long, IdleItem> map=new HashMap<>();
            for(IdleItem idle: idleItemList){
                map.put(idle.getId(),idle);
            }
            for(Order i:list){
                i.setIdleItem(map.get(i.getIdleId()));
            }
        }
        return list;
    }

    /**
     * 查询用户卖出的闲置
     * @param userId
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Order> getMySoldIdle(Long userId){
        List<IdleItem> list= idleItemMapper.getAllIdleItem(userId);
        List<Order> orderList=null;
        if(list.size()>0){
            List<Long> idleIdList=new ArrayList<>();
            for(IdleItem i:list){
                idleIdList.add(i.getId());
            }
            orderList= orderMapper.findOrderByIdleIdList(idleIdList);
            Map<Long, IdleItem> map=new HashMap<>();
            for(IdleItem idle:list){
                map.put(idle.getId(),idle);
            }
            for(Order o:orderList){
                o.setIdleItem(map.get(o.getIdleId()));
            }
        }
        return orderList;
    }

    public PageVo<Order> getAllOrder(int page, int nums){
        List<Order> list= orderMapper.getAllOrder((page-1)*nums,nums);
        if(list.size()>0){
            List<Long> idleIdList=new ArrayList<>();
            for(Order i:list){
                idleIdList.add(i.getIdleId());
            }
            List<IdleItem> idleItemList = idleItemMapper.findIdleByList(idleIdList);
            Map<Long, IdleItem> map=new HashMap<>();
            for(IdleItem idle: idleItemList){
                map.put(idle.getId(),idle);
            }
            for(Order i:list){
                i.setIdleItem(map.get(i.getIdleId()));
            }
        }
        int count= orderMapper.countAllOrder();
        return new PageVo<>(list,count);
    }

    public boolean deleteOrder(long id){
        return orderMapper.deleteByPrimaryKey(id)==1;
    }
}
