package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName("t_schedule_student")
public class StudentSchedule extends BaseModel implements Serializable {

    private Integer scheduleId;
    private Integer studentId;
    private String earning;
    private String selfSummary;
    private Boolean isPresent;
    private Boolean teacherEvolution;
    private String remark;
}
