package io.github.talelin.latticy.vo.course;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SchedulePanelVO {

    private Integer id;
    private String title;
    private String name;
    private String grade;
    private String subject;
    private String teacher;
    private LocalDateTime start;
    private LocalDateTime end;
}
