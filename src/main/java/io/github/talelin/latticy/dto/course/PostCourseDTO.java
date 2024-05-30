package io.github.talelin.latticy.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostCourseDTO {

    private String name;
    private String subject;
    private String grade;
    private Integer teacherId;
    private List<Integer> studentIds;
    private LocalDate startDate;
    private List<CourseDateTime> courseDateTimes;
    private Integer quantity;
    private String remark;

    @Data
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class CourseDateTime {

        private DayOfWeek courseDay;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
