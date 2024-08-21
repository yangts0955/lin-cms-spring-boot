package io.github.talelin.latticy.service.course.strategy.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.mapper.course.ParentMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Parent;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ParentQuery implements QueryStrategy {

    private ParentMapper parentMapper;
    private UserMapper userMapper;

    @Override
    public boolean match(String matter) {
        return RoleEnum.fromValue(matter).equals(RoleEnum.PARENT);
    }

    @Override
    public List<UserSimpleVO> getUserList() {
        QueryWrapper<Parent> queryWrapper = new QueryWrapper<>();
        List<Parent> parents = parentMapper.selectList(queryWrapper);
        return parents.stream().map(parent -> {
            UserSimpleVO userSimpleVO = new UserSimpleVO();
            UserDO userDO = userMapper.selectById(parent.getUserId());
            BeanCopyUtil.copyNonNullProperties(userDO, userSimpleVO);
            userSimpleVO.setId(parent.getId());
            userSimpleVO.setUserId(userDO.getId());
            return userSimpleVO;
        }).toList();
    }
}
