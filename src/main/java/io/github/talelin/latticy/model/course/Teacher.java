package io.github.talelin.latticy.model.course;

import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.BaseModel;
import io.github.talelin.latticy.model.enums.SubjectEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("t_teacher")
public class Teacher extends BaseModel implements Serializable {

    private Integer userId;

    private SubjectEnum subject;
}
