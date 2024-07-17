package com.example.mapper;

import com.example.entity.Course;
import com.example.entity.EchartsData;

import java.util.List;

public interface EchartsMapper {

    List<EchartsData> selectAll();

    List<EchartsData> selectCourse(Course course);
}
