package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum SubjectEnum implements IEnum<String> {

    MATH,
    CHINESE,
    ENGLISH,
    PHYSICS,
    CHEMISTRY,
    BIOLOGY,
    SCIENCE,
    IELTS,
    TOEFL,
    NCE,
    UNKNOWN; //new concept english

    @JsonCreator
    public static SubjectEnum fromValue(String value) {
        for (SubjectEnum subject : SubjectEnum.values()) {
            if (subject.name().equalsIgnoreCase(value)) {
                return subject;
            }
        }
        return getDefault();
    }

    public static SubjectEnum getDefault() {
        return SubjectEnum.UNKNOWN;
    }

    @Override
    public String getValue() {
        return this.name();
    }
}
