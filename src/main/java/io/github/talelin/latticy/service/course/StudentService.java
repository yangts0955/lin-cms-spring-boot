package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.course.Student;

public interface StudentService extends IService<Student> {

    String getStudentNameById(Integer studentId);
}
