package com.example.service;

import com.example.entity.Score;
import com.example.mapper.ScoreMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ScoreService {

    @Resource
    ScoreMapper scoreMapper;

    public PageInfo<Score> selectByCourseName(String courseName, String teacher) {
        List<Score> list = scoreMapper.selectByCourseName(courseName, teacher);
        return PageInfo.of(list);
    }
}
