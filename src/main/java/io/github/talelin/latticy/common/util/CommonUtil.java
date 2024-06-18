package io.github.talelin.latticy.common.util;

import io.github.talelin.latticy.model.enums.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonUtil {

    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    public static GradeEnum getGradeName(String gradeEnum) {
        return ObjectUtils.isEmpty(gradeEnum) ? GradeEnum.getDefault() : GradeEnum.fromValue(gradeEnum);
    }

    public static RoleEnum getRoleName(String roleEnum) {
        return ObjectUtils.isEmpty(roleEnum) ? RoleEnum.getDefault() : RoleEnum.fromValue(roleEnum);
    }

    public static GenderEnum getGenderName(String genderEnum) {
        return ObjectUtils.isEmpty(genderEnum) ? GenderEnum.getDefault() : GenderEnum.fromValue(genderEnum);
    }

    public static SubjectEnum getSubjectName(String subjectEnum) {
        return ObjectUtils.isEmpty(subjectEnum) ? SubjectEnum.getDefault() : SubjectEnum.fromValue(subjectEnum);
    }

    public static CourseStatusEnum getCourseStatus(LocalDateTime courseStartTime, LocalDateTime courseEndTime) {
        if (courseStartTime.isAfter(LocalDateTime.now())) {
            return CourseStatusEnum.TO_START;
        }
        if (courseEndTime.isBefore(LocalDateTime.now())) {
            return CourseStatusEnum.FINISHED;
        }
        return CourseStatusEnum.IN_PROGRESS;
    }

    public static String getDurationStr(Long duration) {
        long hours = duration / 3_600_000;
        long minutes = (duration % 3_600_000) / 60000;
        return hours + "小时" + minutes + "分钟";
    }

    public static Integer calculateGradeSignal(LocalDate entranceDate, GradeEnum originalGrade, GradeEnum newGrade, Integer originalGradeSignal) {
        GradeEnum calGrade = calculateGrade(entranceDate, originalGradeSignal, originalGrade);
        if (ObjectUtils.nullSafeEquals(calGrade, newGrade)) {
            return originalGradeSignal;
        }
        Integer bias = newGrade.ordinal() - calGrade.ordinal();
        return originalGradeSignal + bias;
    }

    public static Integer calculateAge(LocalDate birthday) {
        if (ObjectUtils.isEmpty(birthday)) {
            return null;
        }
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    public static GradeEnum calculateGrade(LocalDate entranceDate, Integer gradeSignal, GradeEnum originalGrade) {
        if (!originalGrade.isLevelUp()) {
            return originalGrade;
        }
        if (ObjectUtils.isEmpty(entranceDate)) {
            if (!ObjectUtils.isEmpty(originalGrade)) {
                return originalGrade;
            }
            return GradeEnum.getDefault();
        }
        Integer gradeNumber = Period.between(entranceDate, LocalDate.now()).getYears() + gradeSignal;
        return GradeEnum.fromSchoolYear(gradeNumber);
    }


    public static LocalDate calculateEntranceDate(GradeEnum gradeEnum) {
        // judge if student level up by July 1st
        if (gradeEnum.isLevelUp()) {
            LocalDate now = LocalDate.now();
            Integer minusYear = gradeEnum.getSchoolYear();
            if (now.getMonthValue() < 7) {
                minusYear++;
            }
            return now.minusYears(minusYear).withMonth(7).withDayOfMonth(1);
        }
        return null;
    }
}
