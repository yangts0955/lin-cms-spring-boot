package io.github.talelin.latticy.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostScheduleDTO {

    private LocalDate courseDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer teacherId;
    private List<Integer> studentIds;
    private Integer courseId;
    private String remark;
}
