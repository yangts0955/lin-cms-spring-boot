package io.github.talelin.latticy.model.mapper;

import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.common.util.EnumUtil;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.model.course.Course;
import io.github.talelin.latticy.model.enums.CourseStatusEnum;
import io.github.talelin.latticy.model.enums.EnumTypeEnum;
import io.github.talelin.latticy.service.course.ScheduleService;
import io.github.talelin.latticy.service.course.StudentService;
import io.github.talelin.latticy.service.course.TeacherService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import io.github.talelin.latticy.vo.course.SchedulePanelVO;
import io.github.talelin.latticy.vo.course.ScheduleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Component
public class CourseConvertor {

    private EnumUtil enumUtil;
    private CourseMapper courseMapper;
    private ScheduleService scheduleService;
    private TeacherService teacherService;
    private StudentService studentService;

    public List<CourseVO> convertCourseToCourseVO(List<Course> courses) {
        if (courses == null) {
            return new ArrayList<>();
        }
        List<CourseVO> result = courses.stream()
                .map(course -> {
                            List<ScheduleVO> schedules = courseMapper.getSchedulesByCourseId(course.getId());
                            return CourseVO.builder()
                                    .courseId(course.getId())
                                    .name(course.getName())
                                    .grade(course.getGrade())
                                    .gradeName(enumUtil.getEnumValueByName(EnumTypeEnum.GRADE.value, course.getGrade().getValue()))
                                    .subject(course.getSubject())
                                    .subjectName(enumUtil.getEnumValueByName(EnumTypeEnum.SUBJECT.value, course.getSubject().getValue()))
                                    .profit(course.getProfit())
                                    .remark(course.getRemark())
                                    .schedules(schedules)
                                    .remainingQuantity(countRemainingQuantity(schedules))
                                    .build();
                        }
                ).toList();
        return buildCoursesVO(result);
    }

    public List<CourseVO> buildCoursesVO(List<CourseVO> courses) {
        return courses.stream()
                .map(courseVO -> {
                            if (courseVO.getSchedules() == null) courseVO.setSchedules(new ArrayList<>());
                            List<Integer> scheduleIds = courseVO.getSchedules().stream().map(ScheduleVO::getScheduleId).toList();
                            if (!scheduleService.batchUpdateSchedulesStatus(scheduleIds)) {
                                throw new FailedException("update schedules status failed");
                            }
                            List<ScheduleVO> scheduleVOS = courseVO.getSchedules().stream()
                                    .map(scheduleVO -> {
                                        scheduleVO.setDurationStr(CommonUtil.getDurationStr(scheduleVO.getDuration()));
                                        scheduleVO.setTeacherName(teacherService.getTeacherNameById(scheduleVO.getTeacherId()));
                                        scheduleVO.setStudentNames(
                                                scheduleVO.getStudentIds().stream()
                                                        .map(id -> studentService.getStudentNameById(id))
                                                        .toList());
                                        return scheduleVO;
                                    })
                                    .sorted(Comparator.comparing((ScheduleVO schedule) -> schedule.getCourseStatus().ordinal())).toList();
                            courseVO.setSchedules(scheduleVOS);
                            return courseVO;
                        }
                ).toList();
    }

    public List<SchedulePanelVO> convertScheduleDetailVOToPanelVO(List<ScheduleDetailVO> scheduleDetails) {
        return scheduleDetails.stream()
                .map(this::convertScheduleDetailToPanel)
                .toList();
    }

    public SchedulePanelVO convertScheduleDetailToPanel(ScheduleDetailVO schedule) {
        return SchedulePanelVO.builder()
                .id(schedule.getScheduleId())
                .title("")
                .name(schedule.getName())
                .grade(enumUtil.getEnumValueByName(EnumTypeEnum.GRADE.value, schedule.getGrade().getValue()))
                .subject(enumUtil.getEnumValueByName(EnumTypeEnum.SUBJECT.value, schedule.getSubject().getValue()))
                .teacher(schedule.getTeacherName())
                .start(CommonUtil.calculateLocalDateTime(schedule.getCourseDate(), schedule.getStartTime()))
                .end(CommonUtil.calculateLocalDateTime(schedule.getCourseDate(), schedule.getEndTime()))
                .build();
    }


    private Integer countRemainingQuantity(List<ScheduleVO> schedules) {
        return (int) schedules.stream()
                .filter(scheduleVO -> !scheduleVO.getCourseStatus().equals(CourseStatusEnum.FINISHED))
                .count();
    }
}
