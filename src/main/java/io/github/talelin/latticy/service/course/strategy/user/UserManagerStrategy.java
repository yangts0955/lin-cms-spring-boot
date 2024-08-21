package io.github.talelin.latticy.service.course.strategy.user;

import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;

import java.util.List;

public interface UserManagerStrategy {

    void register(RegisterDTO registerDTO);

    boolean deleteRelation(Integer userId);

    List<CourseVO> getCourses(Integer userId);

    LinPage<CourseVO> getPageCourses(Integer userId, Integer page, Integer count);

    List<ScheduleDetailVO> getSchedules(Integer userId);
}
