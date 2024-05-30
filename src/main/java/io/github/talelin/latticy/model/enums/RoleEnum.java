package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleEnum implements IEnum<String> {

    ROOT,
    OPERATOR,
    TEACHER,
    STUDENT,
    PARENT,
    GUEST;

    @JsonCreator
    public static RoleEnum fromValue(String value) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        return getDefault();
    }

    public static RoleEnum getDefault() {
        return GUEST;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
