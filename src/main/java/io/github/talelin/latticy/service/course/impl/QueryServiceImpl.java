package io.github.talelin.latticy.service.course.impl;

import io.github.talelin.latticy.common.util.EnumUtil;
import io.github.talelin.latticy.model.enums.EnumTypeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.course.QueryService;
import io.github.talelin.latticy.service.course.strategy.query.QueryStrategy;
import io.github.talelin.latticy.service.course.strategy.rule.sort.RuleSortStrategy;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class QueryServiceImpl implements QueryService {

    private EnumUtil enumUtil;
    private List<QueryStrategy> queryStrategies;

    @Override
    public List<UserSimpleVO> getUserList(RoleEnum role, RuleSortStrategy sortStrategy, String matter) {
        List<UserSimpleVO> userList = new ArrayList<>();
        for (QueryStrategy queryStrategy : queryStrategies) {
            if (queryStrategy.match(role.getValue())) {
                userList = queryStrategy.getUserList();
            }
        }
        userList.forEach(user -> {
            user.setGradeName(enumUtil.getEnumValueByName(EnumTypeEnum.GRADE.value, user.getGrade().name()));
            user.setSubjectName(enumUtil.getEnumValueByName(EnumTypeEnum.SUBJECT.value, user.getSubject().name()));
        });
        if (ObjectUtils.isEmpty(matter)) {
            return userList;
        }
        return sortStrategy.sort(userList, matter);
    }
}
