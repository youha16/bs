package com.example.mwhbs.service.impl;

import com.example.mwhbs.mapper.AdminMapper;
import com.example.mwhbs.entity.Admin;
import com.example.mwhbs.service.AdminService;
import com.example.mwhbs.vo.PageVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    public Admin login(String accountNumber, String adminPassword){
        return adminMapper.login(accountNumber,adminPassword);
    }

    public PageVo<Admin> getAdminList(int page, int nums){
        List<Admin> list= adminMapper.getList((page-1)*nums,nums);
        int count= adminMapper.getCount();
        return new PageVo<>(list,count);
    }

    public boolean addAdmin(Admin admin){
        return adminMapper.insert(admin)==1;
    }
}
