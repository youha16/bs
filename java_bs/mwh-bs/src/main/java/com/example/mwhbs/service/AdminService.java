package com.example.mwhbs.service;

import com.example.mwhbs.entity.Admin;
import com.example.mwhbs.vo.PageVo;

public interface AdminService {

    Admin login(String accountNumber, String adminPassword);

    PageVo<Admin> getAdminList(int page, int nums);

    boolean addAdmin(Admin admin);


}
