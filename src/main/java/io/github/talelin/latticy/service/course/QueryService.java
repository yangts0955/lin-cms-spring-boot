package io.github.talelin.latticy.service.course;

import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.course.strategy.rule.sort.RuleSortStrategy;
import io.github.talelin.latticy.vo.course.UserSimpleVO;

import java.util.List;

public interface QueryService {

    List<UserSimpleVO> getUserList(RoleEnum role, RuleSortStrategy sortStrategy, String matter);

}
