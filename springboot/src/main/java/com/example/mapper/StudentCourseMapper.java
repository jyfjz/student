package com.example.mapper;

import com.example.entity.StudentCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentCourseMapper {
    @Insert("insert into student_course (student_id, course_id, name, no, username) values " +
            "(#{studentId}, #{courseId}, #{name}, #{no}, #{username})")
    void insert(StudentCourse studentCourse);

    @Select("select * from student_course where student_id = #{studentId} and course_id = #{courseId}")
    StudentCourse selectByCondition(StudentCourse studentCourse);

    List<StudentCourse> selectAll(StudentCourse studentCourse);

    @Delete("delete from student_course where student_id=#{studentId} and course_id=#{courseId}")
    void deleteById(Integer studentId, Integer courseId);
}
