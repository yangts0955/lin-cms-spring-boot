package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_course_summary")
public class CourseSummary extends BaseModel implements Serializable {

    private String theme;
    private String target;
    private String description;
    private String coursewareId;
    private String remark;
    private Integer scheduleId;
}
