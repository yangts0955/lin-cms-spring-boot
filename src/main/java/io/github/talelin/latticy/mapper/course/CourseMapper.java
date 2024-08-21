package io.github.talelin.latticy.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.model.course.Course;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleVO;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    List<CourseVO> queryAllCourses();

    List<CourseVO> queryAllCoursesByTeacherId(Integer teacherId);

    List<CourseVO> queryAllCoursesByStudentId(Integer studentId);

    LinPage<Course> queryPageCourseDOsByStudentId(LinPage<Course> page, Integer studentId);

    LinPage<Course> queryPageCourseDOsByTeacherId(LinPage<Course> page, Integer teacherId);

    CourseVO queryByCourseId(Integer courseId);

    List<ScheduleVO> getSchedulesByCourseId(Integer courseId);
}
