package io.github.talelin.latticy.common.util;

import io.github.talelin.latticy.model.enums.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
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
}
