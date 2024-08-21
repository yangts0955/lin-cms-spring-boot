package io.github.talelin.latticy.service.course.strategy.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.mapper.course.TeacherMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TeacherQuery implements QueryStrategy {

    private TeacherMapper teacherMapper;
    private UserMapper userMapper;

    @Override
    public boolean match(String matter) {
        return RoleEnum.fromValue(matter).equals(RoleEnum.TEACHER);
    }

    @Override
    public List<UserSimpleVO> getUserList() {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        List<Teacher> teachers = teacherMapper.selectList(queryWrapper);
        return teachers.stream().map(teacher -> {
            UserSimpleVO userSimpleVO = new UserSimpleVO();
            UserDO userDO = userMapper.selectById(teacher.getUserId());
            BeanCopyUtil.copyNonNullProperties(userDO, userSimpleVO);
            userSimpleVO.setId(teacher.getId());
            userSimpleVO.setUserId(userDO.getId());
            return userSimpleVO;
        }).toList();
    }
}
