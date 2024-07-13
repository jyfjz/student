package com.example.service;

import com.example.entity.Grade;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.GradeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GradeService {

    @Resource
    GradeMapper gradeMapper;

    public void add(Grade grade){
        Grade dbGrade = gradeMapper.selectByStudentIdAndCourseId(grade.getStudentId(), grade.getCourseId());
        if(dbGrade != null){  // 已有同名账号
            throw new CustomException("已经打分过了");
        }
        gradeMapper.insert(grade);
    }

    public PageInfo<Grade> selectPage(Integer pageNum, Integer pageSize, Grade grade) {
        PageHelper.startPage(pageNum, pageSize);
        List<Grade> list = gradeMapper.selectAll(grade);
        return PageInfo.of(list);
    }

    public void update(Grade grade) {
        gradeMapper.update(grade);
    }

    public void deleteById(Integer studentId, Integer courseId) {
        gradeMapper.deleteById(studentId, courseId);
    }
}
