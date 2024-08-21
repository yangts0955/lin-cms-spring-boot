package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.StudentMapper;
import io.github.talelin.latticy.model.course.Student;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private UserService userService;

    @Override
    public String getStudentNameById(Integer studentId) {
        Student student = this.baseMapper.selectById(studentId);
        return userService.getById(student.getUserId()).getRealName();
    }
}
