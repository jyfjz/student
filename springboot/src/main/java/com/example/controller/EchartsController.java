package com.example.controller;

import com.example.common.Result;
import com.example.entity.Course;
import com.example.entity.EchartsData;
import com.example.entity.Grade;
import com.example.service.EchartsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    EchartsService echartsService;

    @GetMapping("/selectCourse")
    public Result selectCourse(Course course){
        PageInfo<EchartsData> pageInfo = echartsService.selectCourse(course);
        return Result.success(pageInfo);
    }

    @GetMapping("/selectAll")
    public Result selectAll(){
        PageInfo<EchartsData> pageInfo = echartsService.selectAll();
        return Result.success(pageInfo);
    }
}
