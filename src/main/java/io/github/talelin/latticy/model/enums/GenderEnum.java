package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum GenderEnum implements IEnum<String> {

    UNKNOWN,
    MALE,
    FEMALE;

    @JsonCreator
    public static GenderEnum fromValue(String value) {
        for (GenderEnum gender : GenderEnum.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return getDefault();
    }

    public static GenderEnum getDefault() {
        return GenderEnum.UNKNOWN;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
