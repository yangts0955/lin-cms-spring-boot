package io.github.talelin.latticy.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 注册数据传输对象
 */
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualField(srcField = "password", dstField = "confirmPassword", message = "{password.equal-field}")
public class RegisterDTO {

    @NotBlank(message = "{username.not-blank}")
    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    private List<Integer> groupIds;

    @Email(message = "{email}")
    private String email;

    @NotBlank(message = "{password.new.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{password.new.pattern}")
    private String password;

    @NotBlank(message = "{password.confirm.not-blank}")
    private String confirmPassword;

    @NotNull(message = "{role.not-null}")
    private String role;

    private String gender;
    private String remark;

    @NotNull(message = "年级不能为空")
    private String grade;

    @NotNull(message = "姓名不能为空")
    private String realName;

    @NotNull(message = "出生日期不能为空")
    private LocalDate birthday;

    private String phoneNumber;

    private String wxNumber;

    private String subject;
}
