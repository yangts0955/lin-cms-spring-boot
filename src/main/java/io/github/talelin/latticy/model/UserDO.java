package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.talelin.latticy.model.enums.GenderEnum;
import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 用户数据对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("lin_user")
@EqualsAndHashCode(callSuper = true)
public class UserDO extends BaseModel implements Serializable {

    private static final long serialVersionUID = -1463999384554707735L;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    @TableField(value = "`gender`")
    private GenderEnum gender;

    private String remark;

    @TableField(value = "`role`")
    private RoleEnum role;

    @TableField(value = "`grade`")
    private GradeEnum grade;
    private Integer gradeSignal;

    private LocalDate birthday;
    private LocalDate entranceDate;
    private String realName;
    private String phoneNumber;
    private String wxNumber;
}
