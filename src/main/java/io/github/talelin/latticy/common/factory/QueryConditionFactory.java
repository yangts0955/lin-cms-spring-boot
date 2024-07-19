package io.github.talelin.latticy.common.factory;

import io.github.talelin.latticy.service.course.strategy.rule.sort.GradeSort;
import io.github.talelin.latticy.service.course.strategy.rule.sort.RuleSortStrategy;
import io.github.talelin.latticy.service.course.strategy.rule.sort.SubjectSort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class QueryConditionFactory {

    private SubjectSort subjectSort;
    private GradeSort gradeSort;

    public RuleSortStrategy getSortStrategy(String strategy) {
        return switch (strategy) {
            case "grade" -> gradeSort;
            case "subject" -> subjectSort;
            default -> null;
        };
    }
}
