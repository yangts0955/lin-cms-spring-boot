package io.github.talelin.latticy.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EnumTypeEnum {

    GENDER(1),
    GRADE(2),
    COURSE_STATUS(3),
    ROLE(4),
    SUBJECT(5);

    public final int value;
}
