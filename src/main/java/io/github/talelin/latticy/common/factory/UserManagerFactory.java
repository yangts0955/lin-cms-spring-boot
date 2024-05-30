package io.github.talelin.latticy.common.factory;

import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.course.strategy.user.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserManagerFactory {

    private OperatorManager operatorManager;
    private TeacherManager teacherManager;
    private StudentManager studentManager;
    private GuestManager guestManager;
    private ParentManager parentManager;

    public UserManagerStrategy getUserStrategy(RoleEnum role) {
        return switch (role) {
            case OPERATOR -> operatorManager;
            case STUDENT -> studentManager;
            case TEACHER -> teacherManager;
            case GUEST -> guestManager;
            case PARENT -> parentManager;
            default -> throw new IllegalArgumentException("Invalid identity");
        };
    }

}
