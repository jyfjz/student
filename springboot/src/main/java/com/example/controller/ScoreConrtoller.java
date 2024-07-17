package com.example.controller;

import com.example.common.Result;
import com.example.entity.Score;
import com.example.service.ScoreService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/scoreRank")
public class ScoreConrtoller {

    @Resource
    ScoreService scoreService;

    @GetMapping("/selectByCourseName")
    public Result selectByCourseName(@RequestParam String courseName,@RequestParam String teacher){
        PageInfo<Score> pageInfo = scoreService.selectByCourseName(courseName, teacher);
        return Result.success(pageInfo);
    }

}
