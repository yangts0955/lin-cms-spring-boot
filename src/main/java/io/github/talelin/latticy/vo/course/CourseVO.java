package io.github.talelin.latticy.vo.course;

import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.SubjectEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseVO {

    private Integer courseId;
    private String name;
    private SubjectEnum subject;
    private GradeEnum grade;
    private BigDecimal profit;
    private List<ScheduleVO> schedules;
}
