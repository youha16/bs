package com.example.mwhbs.service;

import com.example.mwhbs.entity.User;
import com.example.mwhbs.vo.PageVo;

public interface UserService {

    /**
     * 获取某个用户的公开信息
     * @param id
     * @return
     */
    User getUser(Long id);

    /**
     * 登录接口
     * @param accountNumber
     * @param userPassword
     * @return
     */
    User userLogin(String accountNumber, String userPassword);

    /**
     * 注册接口
     * @param user
     * @return
     */
    boolean userSignIn(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    boolean updateUserInfo(User user);

    /**
     * 修改密码
     * @param newPassword
     * @param oldPassword
     * @param id
     * @return
     */
    boolean updatePassword(String newPassword, String oldPassword,Long id);

    PageVo<User> getUserByStatus(int status, int page , int nums);
}
