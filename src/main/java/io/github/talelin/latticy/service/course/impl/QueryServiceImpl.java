package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.common.util.EnumUtil;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.enums.EnumTypeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.course.QueryService;
import io.github.talelin.latticy.service.course.strategy.rule.sort.RuleSortStrategy;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryServiceImpl implements QueryService {

    private UserMapper userMapper;
    private EnumUtil enumUtil;

    @Override
    public List<UserSimpleVO> getUserList(RoleEnum role, RuleSortStrategy strategy, String condition) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getRole, role);
        List<UserDO> userDOS = userMapper.selectList(queryWrapper);
        List<UserSimpleVO> userSimpleVOS = BeanCopyUtil.copyListProperties(userDOS, UserSimpleVO::new);
        userSimpleVOS.forEach(user -> {
            user.setGradeName(enumUtil.getEnumValueByName(EnumTypeEnum.GRADE.value, user.getGrade().name()));
            user.setSubjectName(enumUtil.getEnumValueByName(EnumTypeEnum.SUBJECT.value, user.getSubject().name()));
        });
        if (ObjectUtils.isEmpty(strategy) || ObjectUtils.isEmpty(condition)) {
            return userSimpleVOS;
        }
        return strategy.sort(userSimpleVOS, condition);
    }

}
