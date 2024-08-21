package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.course.Teacher;

public interface TeacherService extends IService<Teacher> {

    String getTeacherNameById(Integer teacherId);
}
