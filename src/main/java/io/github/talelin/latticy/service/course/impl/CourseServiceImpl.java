package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.course.PostCourseDTO;
import io.github.talelin.latticy.dto.course.PostCourseDTO.CourseDateTime;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.dto.course.PutCourseDTO;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.model.course.Course;
import io.github.talelin.latticy.service.course.BatchScheduleService;
import io.github.talelin.latticy.service.course.CourseService;
import io.github.talelin.latticy.service.course.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    private BatchScheduleService batchScheduleService;
    private ScheduleService scheduleService;

    @Override
    @Transactional
    public boolean createCourse(PostCourseDTO courseDTO) {
        Course newCourse = buildCourse(courseDTO.getName(), courseDTO.getSubject(), courseDTO.getGrade());
        boolean courseInsert = this.baseMapper.insert(newCourse) > 0;
        List<PostScheduleDTO> scheduleDTOS = buildPostScheduleDTO(newCourse.getId(), courseDTO);
        boolean batchCreateSchedule = batchScheduleService.batchCreateSchedule(scheduleDTOS);
        return courseInsert && batchCreateSchedule;
    }

    @Override
    public void deleteCourse(Integer courseId) {
        this.baseMapper.deleteById(courseId);
        scheduleService.deleteScheduleByCourseId(courseId);
    }

    @Override
    public void updateCourse(Integer courseId, PutCourseDTO courseDTO) {
        Course course = this.baseMapper.selectById(courseId);
        BeanCopyUtil.copyNonNullProperties(courseDTO, course);
        UpdateWrapper<Course> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Course::getId, courseId);
        this.baseMapper.update(course, updateWrapper);
    }

    private List<PostScheduleDTO> buildPostScheduleDTO(Integer courseId, PostCourseDTO courseDTO) {
        // 从开始时间开始，如果在CourseDateTime列表里的课程时间，就创建，共创建quantity数量的课程
        List<PostScheduleDTO> scheduleDTOS = new ArrayList<>();
        LocalDate startDate = courseDTO.getStartDate();
        int count = 0;
        for (LocalDate start = startDate; start.isBefore(startDate.plusYears(5)) && count < courseDTO.getQuantity(); start = start.plusDays(1)) {
            DayOfWeek dayOfWeek = start.getDayOfWeek();
            for (CourseDateTime schedule : courseDTO.getCourseDateTimes()) {
                if (schedule.getCourseDay().equals(dayOfWeek)) {
                    PostScheduleDTO scheduleDTO = buildPostScheduleDTO(courseId, start, schedule.getStartTime(),
                            schedule.getEndTime(), courseDTO.getTeacherId(), courseDTO.getStudentIds());
                    scheduleDTOS.add(scheduleDTO);
                    count++;
                }
            }
        }

        return scheduleDTOS;
    }

    private Course buildCourse(String name, String subject, String grade) {
        return Course.builder()
                .name(name)
                .subject(CommonUtil.getSubjectName(subject))
                .grade(CommonUtil.getGradeName(grade))
                .build();
    }

    private PostScheduleDTO buildPostScheduleDTO(Integer courseId, LocalDate courseDate, LocalTime startTime,
                                                 LocalTime endTime, Integer teacherId, List<Integer> studentIds) {
        return PostScheduleDTO.builder()
                .courseId(courseId)
                .courseDate(courseDate)
                .startTime(startTime)
                .endTime(endTime)
                .teacherId(teacherId)
                .studentIds(studentIds)
                .build();
    }
}
