package io.github.talelin.latticy.service.course.strategy.rule.sort;

import io.github.talelin.latticy.vo.course.UserSimpleVO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectSort implements RuleSortStrategy {

    @Override
    public List<UserSimpleVO> sort(List<UserSimpleVO> users, String subject) {
        return users.stream()
                .sorted(Comparator.comparing((UserSimpleVO u) -> !u.getSubject().name().equals(subject))
                        .thenComparing(UserSimpleVO::getSubject))
                .collect(Collectors.toList());
    }
}
