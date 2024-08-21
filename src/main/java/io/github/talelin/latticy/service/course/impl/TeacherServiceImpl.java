package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.TeacherMapper;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    private UserService userService;
    @Override
    public String getTeacherNameById(Integer teacherId) {
        Teacher teacher = this.baseMapper.selectById(teacherId);
        return userService.getById(teacher.getUserId()).getRealName();
    }
}
