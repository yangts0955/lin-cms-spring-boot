package io.github.talelin.latticy.service.course.strategy.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.ScheduleService;
import io.github.talelin.latticy.service.course.TeacherService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import io.github.talelin.latticy.vo.course.ScheduleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class TeacherManager implements UserManagerStrategy {

    private TeacherService teacherService;
    private UserService userService;
    private CourseMapper courseMapper;
    private ScheduleService scheduleService;

    @Override
    public void register(RegisterDTO registerDTO) {
        BusinessUtil.assignInnerGroup(registerDTO, InnerGroupEnum.TEACHER);
        UserDO user = userService.createUser(registerDTO);
        teacherService.save(new Teacher(user.getId(), CommonUtil.getSubjectName(registerDTO.getSubject())));
    }

    @Override
    public boolean deleteRelation(Integer userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Teacher::getUserId, userId);
        return teacherService.remove(queryWrapper);
    }

    @Override
    public List<CourseVO> getCourses(Integer userId) {
        Integer teacherId = getTeacherIdByUserId(userId);
        List<CourseVO> courses = courseMapper.queryAllCoursesByTeacherId(teacherId);
        return setCoursesInfo(courses);
    }

    @Override
    public List<ScheduleDetailVO> getSchedules(Integer userId) {
        return BusinessUtil.convertCourseVOtoScheduleVO(this.getCourses(userId));
    }

    private Integer getTeacherIdByUserId(Integer userId) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Teacher::getUserId, userId);
        return teacherService.getOne(queryWrapper).getId();
    }

    private List<CourseVO> setCoursesInfo(List<CourseVO> courses) {
        return courses.stream()
                .map(courseVO -> {
                            List<Integer> scheduleIds = courseVO.getSchedules().stream().map(ScheduleVO::getScheduleId).toList();
                            if (!scheduleService.batchUpdateSchedulesStatus(scheduleIds)) {
                                throw new FailedException("update schedules status failed");
                            }
                            List<ScheduleVO> scheduleVOS = courseVO.getSchedules().stream()
                                    .map(scheduleVO -> {
                                        scheduleVO.setDurationStr(CommonUtil.getDurationStr(scheduleVO.getDuration()));
                                        scheduleVO.setTeacherName(
                                                userService.getById(scheduleVO.getTeacherId())
                                                        .getRealName());
                                        List<String> studentNames = userService.listByIds(scheduleVO.getStudentIds()).stream()
                                                .map(UserDO::getRealName)
                                                .toList();
                                        scheduleVO.setStudentNames(studentNames);
                                        return scheduleVO;
                                    })
                                    .sorted(Comparator.comparing((ScheduleVO schedule) -> schedule.getCourseStatus().ordinal())).toList();
                            courseVO.setSchedules(scheduleVOS);
                            return courseVO;
                        }
                ).toList();
    }
}
