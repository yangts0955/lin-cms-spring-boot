package io.github.talelin.latticy.service.course.strategy.query;

import io.github.talelin.latticy.vo.course.UserSimpleVO;

import java.util.List;

public interface QueryStrategy {

    boolean match(String matter);

    List<UserSimpleVO> getUserList();
}
