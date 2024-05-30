package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import io.github.talelin.latticy.model.enums.CourseStatusEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_schedule")
public class Schedule extends BaseModel implements Serializable {

    private LocalDate courseDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long duration;
    @TableField(value = "`status`")
    private CourseStatusEnum status;
    private Integer courseId;
    private String remark;
}
