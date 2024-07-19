package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.TeacherMapper;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.service.course.TeacherService;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher getByUserId(Integer userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Teacher::getUserId, userId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
