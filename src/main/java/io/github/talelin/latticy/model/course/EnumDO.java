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
@TableName("t_enum_map")
public class EnumDO extends BaseModel implements Serializable {

    private Integer type;
    private String value;
    private String name;
}
