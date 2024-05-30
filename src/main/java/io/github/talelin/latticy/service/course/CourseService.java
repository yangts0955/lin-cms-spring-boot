package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.dto.course.PostCourseDTO;
import io.github.talelin.latticy.dto.course.PutCourseDTO;
import io.github.talelin.latticy.model.course.Course;

public interface CourseService extends IService<Course> {

    boolean createCourse(PostCourseDTO courseDTO);

    void deleteCourse(Integer courseId);

    void updateCourse(Integer courseId, PutCourseDTO courseDTO);
}
