package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.IdleItemMapper;
import com.example.mwhbs.mapper.UserMapper;
import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.entity.User;
import com.example.mwhbs.service.IdleItemService;
import com.example.mwhbs.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IdleItemServiceImpl implements IdleItemService {

    @Resource
    private IdleItemMapper idleItemMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 发布闲置
     * @param idleItem
     * @return
     */
    public boolean addIdleItem(IdleItem idleItem) {
        return idleItemMapper.insert(idleItem) == 1;
    }

    /**
     * 查询闲置信息，同时查出发布者的信息
     * @param id
     * @return
     */
    public IdleItem getIdleItem(Long id) {
        IdleItem idleItem = idleItemMapper.selectByPrimaryKey(id);
        if(idleItem !=null){
            idleItem.setUser(userMapper.selectByPrimaryKey(idleItem.getUserId()));
        }
        return idleItem;
    }

    /**
     * 查询用户发布的所有闲置
     * user_id建索引
     * @param userId
     * @return
     */
    public List<IdleItem> getAllIdelItem(Long userId) {
        return idleItemMapper.getAllIdleItem(userId);
    }

    /**
     * 搜索，分页
     * 同时查出闲置发布者的信息
     * @param findValue
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItem> findIdleItem(String findValue, int page, int nums) {
        List<IdleItem> list= idleItemMapper.findIdleItem(findValue, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItem i:list){
                idList.add(i.getUserId());
            }
            List<User> userList= userMapper.findUserByList(idList);
            Map<Long, User> map=new HashMap<>();
            for(User user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItem i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count= idleItemMapper.countIdleItem(findValue);
        return new PageVo<>(list,count);
    }

    /**
     * 分类查询，分页
     * 同时查出闲置发布者的信息，代码结构与上面的类似，可封装优化，或改为join查询
     * @param idleLabel
     * @param page
     * @param nums
     * @return
     */
    public PageVo<IdleItem> findIdleItemByLable(int idleLabel, int page, int nums) {
        List<IdleItem> list= idleItemMapper.findIdleItemByLable(idleLabel, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItem i:list){
                idList.add(i.getUserId());
            }
            List<User> userList= userMapper.findUserByList(idList);
            Map<Long, User> map=new HashMap<>();
            for(User user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItem i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count= idleItemMapper.countIdleItemByLable(idleLabel);
        return new PageVo<>(list,count);
    }

    /**
     * 更新闲置信息
     * @param idleItem
     * @return
     */
    public boolean updateIdleItem(IdleItem idleItem){
        return idleItemMapper.updateByPrimaryKeySelective(idleItem)==1;
    }

    public PageVo<IdleItem> adminGetIdleList(int status, int page, int nums) {
        List<IdleItem> list= idleItemMapper.getIdleItemByStatus(status, (page - 1) * nums, nums);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(IdleItem i:list){
                idList.add(i.getUserId());
            }
            List<User> userList= userMapper.findUserByList(idList);
            Map<Long, User> map=new HashMap<>();
            for(User user:userList){
                map.put(user.getId(),user);
            }
            for(IdleItem i:list){
                i.setUser(map.get(i.getUserId()));
            }
        }
        int count= idleItemMapper.countIdleItemByStatus(status);
        return new PageVo<>(list,count);
    }
}
