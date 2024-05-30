package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.dto.course.PutScheduleDTO;
import io.github.talelin.latticy.mapper.course.*;
import io.github.talelin.latticy.model.course.*;
import io.github.talelin.latticy.service.course.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    private TeacherScheduleMapper teacherScheduleMapper;
    private StudentScheduleMapper studentScheduleMapper;
    private AccountingSummaryMapper accountingSummaryMapper;
    private CourseSummaryMapper courseSummaryMapper;

    @Override
    @Transactional
    public boolean createSchedule(PostScheduleDTO scheduleDTO) {
        Schedule schedule = buildSchedule(scheduleDTO);
        boolean scheduleInsert = this.baseMapper.insert(schedule) > 0;
        boolean teacherScheduleInsert = bindTeacherSchedule(scheduleDTO.getTeacherId(), schedule.getId());
        boolean studentScheduleInsert = bindStudentSchedule(scheduleDTO.getStudentIds(), schedule.getId());
        return scheduleInsert && teacherScheduleInsert && studentScheduleInsert;
    }

    @Override
    public void deleteScheduleByCourseId(Integer courseId) {
        QueryWrapper<Schedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Schedule::getCourseId, courseId);
        List<Schedule> schedules = this.baseMapper.selectList(queryWrapper);
        this.baseMapper.delete(queryWrapper);

        // 删除关联表
        schedules.forEach(schedule -> deleteScheduleRelation(schedule.getId()));
    }

    @Override
    public void deleteScheduleById(Integer scheduleId) {
        this.baseMapper.deleteById(scheduleId);
        deleteScheduleRelation(scheduleId);
    }

    @Override
    public void updateSchedule(Integer scheduleId, PutScheduleDTO scheduleDTO) {
        Schedule schedule = buildSchedule(scheduleDTO);
        schedule.setId(scheduleId);
        this.baseMapper.updateById(schedule);
        if (!ObjectUtils.isEmpty(scheduleDTO.getTeacherId())) {
            updateTeacherSchedule(scheduleDTO.getTeacherId(), scheduleId);
        }
        if (!ObjectUtils.isEmpty(scheduleDTO.getStudentIds())) {
            updateStudentSchedule(scheduleDTO.getStudentIds(), scheduleId);
        }
    }

    private void deleteScheduleRelation(Integer scheduleId) {
        // delete teacher schedule
        QueryWrapper<TeacherSchedule> teacherScheduleWrapper = new QueryWrapper<>();
        teacherScheduleWrapper.lambda().eq(TeacherSchedule::getScheduleId, scheduleId);
        teacherScheduleMapper.delete(teacherScheduleWrapper);

        // delete student schedule
        QueryWrapper<StudentSchedule> studentScheduleWrapper = new QueryWrapper<>();
        studentScheduleWrapper.lambda().eq(StudentSchedule::getScheduleId, scheduleId);
        studentScheduleMapper.delete(studentScheduleWrapper);

        // delete accounting summary
        QueryWrapper<AccountingSummary> accountingSummaryWrapper = new QueryWrapper<>();
        accountingSummaryWrapper.lambda().eq(AccountingSummary::getScheduleId, scheduleId);
        accountingSummaryMapper.delete(accountingSummaryWrapper);

        // delete course summary
        QueryWrapper<CourseSummary> courseSummaryWrapper = new QueryWrapper<>();
        courseSummaryWrapper.lambda().eq(CourseSummary::getScheduleId, scheduleId);
        courseSummaryMapper.delete(courseSummaryWrapper);
    }

    private void updateTeacherSchedule(Integer teacherId, Integer scheduleId) {
        QueryWrapper<TeacherSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TeacherSchedule::getScheduleId, scheduleId);
        teacherScheduleMapper.delete(queryWrapper);
        bindTeacherSchedule(teacherId, scheduleId);
    }

    private void updateStudentSchedule(List<Integer> students, Integer scheduleId) {
        QueryWrapper<StudentSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StudentSchedule::getScheduleId, scheduleId);
        studentScheduleMapper.delete(queryWrapper);
        bindStudentSchedule(students, scheduleId);
    }

    private boolean bindTeacherSchedule(Integer teacherId, Integer scheduleId) {
        TeacherSchedule teacherSchedule = buildTeacherSchedule(teacherId, scheduleId);
        return teacherScheduleMapper.insert(teacherSchedule) > 0;
    }

    private boolean bindStudentSchedule(List<Integer> studentIds, Integer scheduleId) {
        return studentIds.stream().allMatch(studentId -> {
            StudentSchedule studentSchedule = buildStudentSchedule(studentId, scheduleId);
            return studentScheduleMapper.insert(studentSchedule) > 0;
        });
    }

    private Schedule buildSchedule(PostScheduleDTO scheduleDTO) {
        LocalDate courseDate = scheduleDTO.getCourseDate();
        LocalTime startTime = scheduleDTO.getStartTime();
        LocalTime endTime = scheduleDTO.getEndTime();
        LocalDateTime startDateTime = courseDate.atTime(startTime);
        LocalDateTime endDateTime = courseDate.atTime(endTime);
        return Schedule.builder()
                .courseDate(courseDate)
                .startTime(startTime)
                .endTime(endTime)
                .duration(Duration.between(startTime, endTime).toMillis())
                .status(CommonUtil.getCourseStatus(startDateTime, endDateTime))
                .courseId(scheduleDTO.getCourseId())
                .remark(scheduleDTO.getRemark())
                .build();
    }

    private Schedule buildSchedule(PutScheduleDTO scheduleDTO) {
        LocalDate courseDate = scheduleDTO.getCourseDate();
        LocalTime startTime = scheduleDTO.getStartTime();
        LocalTime endTime = scheduleDTO.getEndTime();
        LocalDateTime startDateTime = courseDate.atTime(startTime);
        LocalDateTime endDateTime = courseDate.atTime(endTime);
        return Schedule.builder()
                .courseDate(courseDate)
                .startTime(startTime)
                .endTime(endTime)
                .duration(Duration.between(startTime, endTime).toMillis())
                .status(CommonUtil.getCourseStatus(startDateTime, endDateTime))
                .courseId(scheduleDTO.getCourseId())
                .remark(scheduleDTO.getRemark())
                .build();
    }

    private TeacherSchedule buildTeacherSchedule(Integer teacherId, Integer scheduleId) {
        return TeacherSchedule.builder()
                .teacherId(teacherId)
                .scheduleId(scheduleId)
                .build();
    }

    private StudentSchedule buildStudentSchedule(Integer studentId, Integer scheduleId) {
        return StudentSchedule.builder()
                .studentId(studentId)
                .scheduleId(scheduleId)
                .build();
    }
}
