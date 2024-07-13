package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;

    // 登录逻辑
    public Account login(Account account){
        Account dbadmin = adminMapper.selectByUsername(account.getUsername());
        if(dbadmin == null){
            throw new CustomException("账号或密码错误。");
        }
        // 比较密码
        if(!account.getPassword().equals(dbadmin.getPassword())){
            throw new CustomException("账号或密码错误。");
        }
        // 登录成功
        return dbadmin;
    }
}
