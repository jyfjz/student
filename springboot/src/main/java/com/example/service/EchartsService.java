package com.example.service;

import com.example.entity.Course;
import com.example.entity.EchartsData;
import com.example.mapper.EchartsMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EchartsService {
    @Resource
    EchartsMapper echartsMapper;

    public PageInfo<EchartsData> selectAll() {
        List<EchartsData> list = echartsMapper.selectAll();
        return PageInfo.of(list);
    }

    public PageInfo<EchartsData> selectCourse(Course course) {
        List<EchartsData> list = echartsMapper.selectCourse(course);
        return PageInfo.of(list);
    }
}
