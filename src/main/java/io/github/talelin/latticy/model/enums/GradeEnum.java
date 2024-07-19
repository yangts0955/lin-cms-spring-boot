package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum GradeEnum implements IEnum<String> {
    FIRST_GRADE(1),
    SECOND_GRADE(2),
    THIRD_GRADE(3),
    FOURTH_GRADE(4),
    FIFTH_GRADE(5),
    SIXTH_GRADE(6),
    SEVENTH_GRADE(7),
    EIGHTH_GRADE(8),
    NINTH_GRADE(9),
    SENIOR_ONE(10),
    SENIOR_TWO(11),
    SENIOR_THREE(12),
    SENIOR_HIGH(-1),
    UNDERGRADUATE(-1),
    POSTGRADUATE_PREPARATION(-1),
    PUBLIC_SERVANT_PREPARATION(-1),
    OTHER(-1);

    private final Integer schoolYear;

    @JsonCreator
    public static GradeEnum fromValue(String value) {
        for (GradeEnum grade : GradeEnum.values()) {
            if (grade.name().equalsIgnoreCase(value)) {
                return grade;
            }
        }
        return getDefault();
    }

    public static GradeEnum fromSchoolYear(Integer schoolYear) {
        for (GradeEnum grade : GradeEnum.values()) {
            if (Objects.equals(grade.getSchoolYear(), schoolYear)) {
                return grade;
            }
        }
        return getDefault();
    }

    public static GradeEnum getDefault() {
        return GradeEnum.OTHER;
    }

    public boolean isLevelUp() {
        return schoolYear > 0;
    }

    @Override
    public String getValue() {
        return this.name();
    }

    public int compare(GradeEnum g) {
        return Math.abs(g.ordinal() - this.ordinal());
    }
}
