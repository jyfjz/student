package com.example.mapper;

import com.example.entity.Score;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ScoreMapper {

    List<Score> selectByCourseName(String courseName, String teacher);
}
