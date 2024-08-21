package io.github.talelin.latticy.vo.course;

import io.github.talelin.latticy.model.enums.GenderEnum;
import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.model.enums.SubjectEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSimpleVO {

    private Integer id;
    private Integer userId;
    private Integer age;
    private String username;
    private String nickname;
    private String avatar;
    private GenderEnum gender;
    private String remark;
    private RoleEnum role;
    private GradeEnum grade;
    private String gradeName;
    private SubjectEnum subject;
    private String subjectName;
    private String realName;
}
