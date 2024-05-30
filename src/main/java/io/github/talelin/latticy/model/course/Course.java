package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.SubjectEnum;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_course")
public class Course extends BaseModel implements Serializable {

    private String name;
    @TableField(value = "`subject`")
    private SubjectEnum subject;
    @TableField(value = "`grade`")
    private GradeEnum grade;
    private BigDecimal profit;
    private String remark;
}
