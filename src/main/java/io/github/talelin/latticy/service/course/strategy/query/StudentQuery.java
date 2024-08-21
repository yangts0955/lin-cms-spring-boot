package io.github.talelin.latticy.service.course.strategy.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.mapper.course.StudentMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Student;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class StudentQuery implements QueryStrategy {

    private StudentMapper studentMapper;
    private UserMapper userMapper;

    @Override
    public boolean match(String matter) {
        return RoleEnum.fromValue(matter).equals(RoleEnum.STUDENT);
    }

    @Override
    public List<UserSimpleVO> getUserList() {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        List<Student> students = studentMapper.selectList(queryWrapper);
        return students.stream().map(student -> {
            UserSimpleVO userSimpleVO = new UserSimpleVO();
            UserDO userDO = userMapper.selectById(student.getUserId());
            BeanCopyUtil.copyNonNullProperties(userDO, userSimpleVO);
            userSimpleVO.setId(student.getId());
            userSimpleVO.setUserId(userDO.getId());
            return userSimpleVO;
        }).toList();
    }
}
