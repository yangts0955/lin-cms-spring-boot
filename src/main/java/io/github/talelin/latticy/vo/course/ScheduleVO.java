package io.github.talelin.latticy.vo.course;

import io.github.talelin.latticy.model.enums.CourseStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleVO {

    private Integer scheduleId;
    private LocalDate courseDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long duration;
    private String durationStr;
    private CourseStatusEnum courseStatus;
    private String remark;
    private BigDecimal profit;
    private Integer teacherId;
    private String teacherName;
    private List<Integer> studentIds;
    private List<String> studentNames;
}
