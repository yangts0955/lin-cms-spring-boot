package io.github.talelin.latticy.service.course.strategy.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.TeacherService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import io.github.talelin.latticy.vo.course.ScheduleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class TeacherManager implements UserManagerStrategy {

    private TeacherService teacherService;
    private UserService userService;
    private CourseMapper courseMapper;

    @Override
    public void register(RegisterDTO registerDTO) {
        BusinessUtil.assignInnerGroup(registerDTO, InnerGroupEnum.TEACHER);
        UserDO user = userService.createUser(registerDTO);
        teacherService.save(new Teacher(user.getId()));
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
        for (CourseVO course : courses) {
            List<ScheduleVO> scheduleVOS = new ArrayList<>();
            for (ScheduleVO scheduleVO : course.getSchedules()) {
                if (scheduleVO.getTeacherId().equals(teacherId)) {
                    scheduleVO.setDurationStr(CommonUtil.getDurationStr(scheduleVO.getDuration()));
                    scheduleVOS.add(scheduleVO);
                }
            }
            course.setSchedules(scheduleVOS);
        }
        return courses;
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
}
