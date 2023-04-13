package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.IdleItemMapper;
import com.example.mwhbs.mapper.MessageMapper;
import com.example.mwhbs.mapper.UserMapper;
import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.entity.Message;
import com.example.mwhbs.entity.User;
import com.example.mwhbs.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private IdleItemMapper idleItemMapper;

    /**
     * 增加一条留言
     * @param message
     * @return
     */
    public boolean addMessage(Message message){
        return messageMapper.insert(message)==1;
    }

    /**
     * 删除一条留言，未做用户身份验证
     * @param id
     * @return
     */
    public boolean deleteMessage(Long id){
        return messageMapper.deleteByPrimaryKey(id)==1;
    }

    /**
     * 获取一条留言
     * @param id
     * @return
     */
    public Message getMessage(Long id){
        return messageMapper.selectByPrimaryKey(id);
    }

    /**
     * 获取一个用户收到的所有留言，未做分页查询
     * 同时查询出用户的信息和闲置的信息
     * userId建索引
     * @param userId
     * @return
     */
    public List<Message> getAllMyMessage(Long userId){
        List<Message> list= messageMapper.getMyMessage(userId);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(Message i:list){
                idList.add(i.getUserId());
            }
            List<User> userList= userMapper.findUserByList(idList);
            Map<Long, User> map=new HashMap<>();
            for(User user:userList){
                map.put(user.getId(),user);
            }
            for(Message i:list){
                i.setFromU(map.get(i.getUserId()));
            }

            List<Long> idleIdList=new ArrayList<>();
            for(Message i:list){
                idleIdList.add(i.getIdleId());
            }
            List<IdleItem> idleList= idleItemMapper.findIdleByList(idleIdList);
            Map<Long, IdleItem> idleMap=new HashMap<>();
            for(IdleItem idle:idleList){
                idleMap.put(idle.getId(),idle);
            }
            for(Message i:list){
                i.setIdle(idleMap.get(i.getIdleId()));
            }
        }
        return list;
    }

    /**
     * 查询一个闲置下的所有留言，未做分页
     * 同时查出发送者和接收者的信息
     * idleId建索引
     * @param idleId
     * @return
     */
    public List<Message> getAllIdleMessage(Long idleId){
        List<Message> list= messageMapper.getIdleMessage(idleId);
        if(list.size()>0){
            List<Long> idList=new ArrayList<>();
            for(Message i:list){
                idList.add(i.getUserId());
            }
            List<User> userList= userMapper.findUserByList(idList);
            Map<Long, User> map=new HashMap<>();
            for(User user:userList){
                map.put(user.getId(),user);
            }
            for(Message i:list){
                i.setFromU(map.get(i.getUserId()));
            }
            Map<Long, Message> mesMap=new HashMap<>();
            for(Message i:list){
                mesMap.put(i.getId(),i);
            }
            for(Message i:list){
                Message toM=new Message();
                User toU=new User();
                if(i.getToMessage()!=null){
                    toM.setContent(mesMap.get(i.getToMessage()).getContent());
                    toU.setNickname(map.get(i.getToUser()).getNickname());
                }
                i.setToM(toM);
                i.setToU(toU);
            }
        }
        return list;
    }
}
