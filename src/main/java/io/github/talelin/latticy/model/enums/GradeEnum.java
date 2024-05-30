package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum GradeEnum implements IEnum<String> {
    FIRST_GRADE,
    SECOND_GRADE,
    THIRD_GRADE,
    FOURTH_GRADE,
    FIFTH_GRADE,
    SIXTH_GRADE,
    SEVENTH_GRADE,
    EIGHTH_GRADE,
    NINTH_GRADE,
    SENIOR_ONE,
    SENIOR_TWO,
    SENIOR_THREE,
    SENIOR_HIGH,
    UNDERGRADUATE,
    POSTGRADUATE_PREPARATION,
    PUBLIC_SERVANT_PREPARATION,
    OTHER;

    @JsonCreator
    public static GradeEnum fromValue(String value) {
        for (GradeEnum grade : GradeEnum.values()) {
            if (grade.name().equalsIgnoreCase(value)) {
                return grade;
            }
        }
        return getDefault();
    }

    public static GradeEnum getDefault() {
        return GradeEnum.OTHER;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
