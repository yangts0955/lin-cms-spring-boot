package io.github.talelin.latticy.common.util;

import static org.springframework.beans.BeanUtils.copyProperties;

import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BusinessUtil {

    public static void assignInnerGroup(RegisterDTO registerDTO, InnerGroupEnum group) {
        if (ObjectUtils.isEmpty(registerDTO.getGroupIds())) {
            registerDTO.setGroupIds(List.of(group.getGroupId()));
        } else {
            List<Integer> groupsIds = new ArrayList<>(registerDTO.getGroupIds());
            groupsIds.add(group.getGroupId());
            registerDTO.setGroupIds(groupsIds);
        }
    }

    public static List<ScheduleDetailVO> convertCourseVOtoScheduleVO(List<CourseVO> course) {
        return course.stream()
                .flatMap(courseVO ->
                        courseVO.getSchedules().stream().flatMap(scheduleVO -> {
                            ScheduleDetailVO scheduleDetailVO = new ScheduleDetailVO();
                            copyProperties(courseVO, scheduleDetailVO);
                            copyProperties(scheduleVO, scheduleDetailVO);
                            return Stream.of(scheduleDetailVO);
                        })
                ).sorted(Comparator.comparing(ScheduleDetailVO::getCourseDate))
                .toList();
    }
}
