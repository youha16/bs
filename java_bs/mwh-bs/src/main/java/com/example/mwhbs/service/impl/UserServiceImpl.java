package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.UserMapper;
import com.example.mwhbs.entity.User;
import com.example.mwhbs.service.UserService;
import com.example.mwhbs.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;



@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    public User getUser(Long id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 登录，安全问题未解决
     * @param accountNumber
     * @param userPassword
     * @return
     */
    public User userLogin(String accountNumber, String userPassword){
        return userMapper.userLogin(accountNumber,userPassword);
    }

    public boolean userSignIn(User user){
        return userMapper.insert(user) == 1;
    }

    /**
     *修改用户公开信息，未验证用户身份
     * @param user
     * @return
     */
    public boolean updateUserInfo(User user){
        return userMapper.updateByPrimaryKeySelective(user)==1;
    }

    /**
     * 修改密码
     * @param newPassword
     * @param oldPassword
     * @param id
     * @return
     */
    public boolean updatePassword(String newPassword, String oldPassword,Long id){
        return userMapper.updatePassword(newPassword,oldPassword,id)==1;
    }

    public PageVo<User> getUserByStatus(int status, int page , int nums){
        List<User> list;
        int count=0;
        if(status==0){
            count= userMapper.countNormalUser();
            list= userMapper.getNormalUser((page-1)*nums, nums);
        }else {
            count= userMapper.countBanUser();
            list= userMapper.getBanUser((page-1)*nums, nums);
        }
        return new PageVo<>(list,count);
    }

}
