package io.github.talelin.latticy.service.course.strategy.rule.sort;

import io.github.talelin.latticy.vo.course.UserSimpleVO;

import java.util.List;

public interface RuleSortStrategy {

    List<UserSimpleVO> sort(List<UserSimpleVO> users, String value);
}
