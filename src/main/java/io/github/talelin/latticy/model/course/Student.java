package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import io.github.talelin.latticy.model.enums.GradeEnum;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@TableName("t_student")
public class Student extends BaseModel implements Serializable {

    @TableField(value = "`grade`")
    private GradeEnum grade;
    private Integer parentId;
    private Integer userId;
}
