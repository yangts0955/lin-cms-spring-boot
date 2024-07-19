package io.github.talelin.latticy.service.course.strategy.rule.sort;

import static io.github.talelin.latticy.common.util.CommonUtil.getGradeName;

import io.github.talelin.latticy.vo.course.UserSimpleVO;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GradeSort implements RuleSortStrategy {
    @Override
    public List<UserSimpleVO> sort(List<UserSimpleVO> users, String grade) {
        return users.stream().sorted(Comparator.comparing((UserSimpleVO u) -> !u.getGrade().name().equals(grade))
                        .thenComparing((UserSimpleVO u) -> u.getGrade().compare(getGradeName(grade))))
                .collect(Collectors.toList());
    }
}
