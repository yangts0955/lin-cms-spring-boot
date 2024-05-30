package io.github.talelin.latticy.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.course.Course;
import io.github.talelin.latticy.vo.course.CourseVO;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVO> queryAllCourses();

    List<CourseVO> queryAllCoursesByTeacherId(Integer teacherId);

    List<CourseVO> queryAllCoursesByStudentId(Integer studentId);
}
