package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.FavoriteMapper;
import com.example.mwhbs.mapper.IdleItemMapper;
import com.example.mwhbs.entity.Favorite;
import com.example.mwhbs.entity.IdleItem;
import com.example.mwhbs.service.FavoriteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Resource
    private IdleItemMapper idleItemMapper;

    /**
     * 新增收藏
     * @param favorite
     * @return
     */
    public boolean addFavorite(Favorite favorite){
        return favoriteMapper.insert(favorite)==1;
    }

    /**
     * 删除收藏
     * @param id
     * @return
     */
    public boolean deleteFavorite(Long id){
        return favoriteMapper.deleteByPrimaryKey(id)==1;
    }

    /**
     * 判断用户是否收藏某个闲置
     * user_id建索引
     * @param userId
     * @param idleId
     * @return
     */
    public Integer isFavorite(Long userId,Long idleId){
        return favoriteMapper.checkFavorite(userId,idleId);
    }

    /**
     * 查询一个用户的所有收藏
     * 关联查询，没有用join，通过where in查询关联的闲置信息
     * @param userId
     * @return
     */
    public List<Favorite> getAllFavorite(Long userId){
        List<Favorite> list= favoriteMapper.getMyFavorite(userId);
        if(list.size()>0){
            List<Long> idleIdList=new ArrayList<>();
            for(Favorite i:list){
                idleIdList.add(i.getIdleId());
            }
            List<IdleItem> idleItemList = idleItemMapper.findIdleByList(idleIdList);
            Map<Long, IdleItem> map=new HashMap<>();
            for(IdleItem idle: idleItemList){
                map.put(idle.getId(),idle);
            }
            for(Favorite i:list){
                i.setIdleItem(map.get(i.getIdleId()));
            }
        }
        return list;
    }
}
