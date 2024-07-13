package com.example.service;

import com.example.entity.Course;
import com.example.mapper.CourseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CourseService {

    @Resource
    private CourseMapper courseMapper;

    // total 查询出来的总数， list是数据列表
    public PageInfo<Course> selectPage(Integer pageNum, Integer pageSize, Course course){
        PageHelper.startPage(pageNum, pageSize);        // 帮忙分页
        List<Course> courseList = courseMapper.selectAll(course);
        return PageInfo.of(courseList);
    }
    // 新增课程数据
    public void add(Course course) {
        courseMapper.insert(course);
    }

    public void updateById(Course course) {
        courseMapper.updateById(course);
    }

    public void deleteById(Integer id) {
        courseMapper.deleteById(id);
    }
}
