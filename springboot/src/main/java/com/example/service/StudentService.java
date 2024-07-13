package com.example.service;


import cn.hutool.core.util.ObjectUtil;
import com.example.common.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.AdminMapper;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {
    @Resource
    private StudentMapper studentMapper;

    // 登录逻辑
    public Account login(Account account){
        Account dbStudent = studentMapper.selectByUsername(account.getUsername());
        if(dbStudent == null){
            throw new CustomException("账号或密码错误。");
        }
        // 比较密码
        if(!account.getPassword().equals(dbStudent.getPassword())){
            throw new CustomException("账号或密码错误。");
        }
        // 登录成功
        return dbStudent;
    }

    public void register(Account account) {
        Student student = new Student();
        student.setUsername(account.getUsername());
        student.setPassword(account.getPassword());
        this.add(student);
    }

    // 新增数据库元素
    public void add(Student student) {
        Student dbStudent = studentMapper.selectByUsername(student.getUsername());
        if(dbStudent != null){  // 已有同名账号
            throw new CustomException("账号已经存在");
        }
        if(ObjectUtil.isEmpty(student.getName())){
            student.setName(student.getUsername());
        }
        student.setRole(RoleEnum.STUDENT.name());
        studentMapper.insert(student);
    }

    public void deleteById(Integer id) {
        studentMapper.deletById(id);
    }

    public void updateById(Student student) {
        studentMapper.updateById(student);
    }

    public PageInfo<Student> selectPage(Integer pageNum, Integer pageSize, Student student) {
        PageHelper.startPage(pageNum, pageSize);
        List<Student> studentList = studentMapper.selectAll(student);
        return PageInfo.of(studentList);
    }
}
