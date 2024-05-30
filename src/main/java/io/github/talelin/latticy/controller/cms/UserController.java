package io.github.talelin.latticy.controller.cms;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.core.annotation.RefreshRequired;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.configuration.LoginCaptchaProperties;
import io.github.talelin.latticy.common.factory.UserManagerFactory;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.LoginDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.UserIdentityService;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.strategy.user.UserManagerStrategy;
import io.github.talelin.latticy.vo.*;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@RestController
@RequestMapping("/cms/user")
@PermissionModule(value = "用户")
@Validated
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private GroupService groupService;
    private UserIdentityService userIdentityService;
    private DoubleJWT jwt;
    private LoginCaptchaProperties captchaConfig;
    private UserManagerFactory userManagerFactory;

    /**
     * 用户注册
     */
    @PostMapping("/register")
//    @GroupRequired
    @PermissionMeta("注册用户")
    public CreatedVO register(@RequestBody @Validated RegisterDTO registerDTO) {
        UserManagerStrategy strategy = userManagerFactory.getUserStrategy(CommonUtil.getRoleName(registerDTO.getRole()));
        strategy.register(registerDTO);
        return new CreatedVO(11);
    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated LoginDTO validator, @RequestHeader(required = false) String tag) {
        if (Boolean.TRUE.equals(captchaConfig.getEnabled())) {
            if (!StringUtils.hasText(validator.getCaptcha()) || !StringUtils.hasText(tag)) {
                throw new ParameterException(10260);
            }
            if (!userService.verifyCaptcha(validator.getCaptcha(), tag)) {
                throw new ParameterException(10260);
            }
        }
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException(10021);
        }
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        if (!valid) {
            throw new ParameterException(10031);
        }
        return jwt.generateTokens(user.getId());
    }

    @PostMapping("/captcha")
    public LoginCaptchaVO userCaptcha() throws Exception {
        if (Boolean.TRUE.equals(captchaConfig.getEnabled())) {
            return userService.generateCaptcha();
        }
        return new LoginCaptchaVO();
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    @LoginRequired
    public UpdatedVO update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return new UpdatedVO(6);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change_password")
    @LoginRequired
    public UpdatedVO updatePassword(@RequestBody @Validated ChangePasswordDTO validator) {
        userService.changeUserPassword(validator);
        return new UpdatedVO(4);
    }

    /**
     * 刷新令牌
     */
    @GetMapping("/refresh")
    @RefreshRequired
    public Tokens getRefreshToken() {
        UserDO user = LocalUser.getLocalUser();
        return jwt.generateTokens(user.getId());
    }

    /**
     * 查询拥有权限
     */
    @GetMapping("/permissions")
    @LoginRequired
    public UserPermissionVO getPermissions() {
        UserDO user = LocalUser.getLocalUser();
        boolean admin = groupService.checkIsRootByUserId(user.getId());
        List<Map<String, List<Map<String, String>>>> permissions = userService.getStructuralUserPermissions(user.getId());
        UserPermissionVO userPermissions = new UserPermissionVO(user, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 查询自己信息
     */
    @GetMapping("/information")
    @LoginRequired
    public UserInfoVO getInformation() {
        UserDO user = LocalUser.getLocalUser();
        List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }
}
