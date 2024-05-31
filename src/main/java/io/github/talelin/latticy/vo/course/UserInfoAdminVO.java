package io.github.talelin.latticy.vo.course;

import static io.github.talelin.latticy.common.util.CommonUtil.calculateAge;
import static io.github.talelin.latticy.common.util.CommonUtil.calculateGrade;

import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoAdminVO {
    private Integer id;

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

    /**
     * 分组
     */
    private List<GroupDO> groups;

    private Integer age;

    private String gender;

    private String remark;

    private String role;

    private String grade;

    private String realName;

    private String phoneNumber;

    private String wxNumber;


    public UserInfoAdminVO(UserDO user, List<GroupDO> groups) {
        BeanUtils.copyProperties(user, this);
        this.groups = groups;
        this.age = calculateAge(user.getBirthday());
        this.grade = calculateGrade(user.getEntranceDate(), user.getGradeSignal(), user.getGrade()).name();
        this.gender = user.getGender().name();
        this.role = user.getRole().name();
    }
}
