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
@TableName("t_schedule_teacher")
public class TeacherSchedule extends BaseModel implements Serializable {

    private Integer scheduleId;
    private Integer teacherId;
    private String salary;
    private String summary;
    private Boolean isPresent;
    private String remark;
}
