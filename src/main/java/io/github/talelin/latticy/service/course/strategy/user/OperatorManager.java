package io.github.talelin.latticy.service.course.strategy.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Course;
import io.github.talelin.latticy.model.course.Operator;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.model.mapper.CourseConvertor;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.OperatorService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OperatorManager implements UserManagerStrategy {

    private OperatorService operatorService;
    private UserService userService;
    private CourseMapper courseMapper;
    private CourseConvertor courseConvertor;

    @Override
    public void register(RegisterDTO registerDTO) {
        BusinessUtil.assignInnerGroup(registerDTO, InnerGroupEnum.OPERATOR);
        UserDO user = userService.createUser(registerDTO);
        operatorService.save(new Operator(user.getId()));
    }

    @Override
    public boolean deleteRelation(Integer userId) {
        QueryWrapper<Operator> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Operator::getUserId, userId);
        return operatorService.remove(queryWrapper);
    }

    @Override
    public List<CourseVO> getCourses(Integer userId) {
        List<CourseVO> courses = courseMapper.queryAllCourses();
        return courseConvertor.buildCoursesVO(courses);
    }

    @Override
    public LinPage<CourseVO> getPageCourses(Integer userId, Integer page, Integer count) {
        LinPage<Course> linPage = new LinPage<>(page, count);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        LinPage<Course> courses = courseMapper.selectPage(linPage, queryWrapper);
        LinPage<CourseVO> courseVOs = new LinPage<>();
        BeanCopyUtil.copyNonNullProperties(courses, courseVOs);
        courseVOs.setRecords(courseConvertor.convertCourseToCourseVO(courses.getRecords()));
        return courseVOs;
    }

    @Override
    public List<ScheduleDetailVO> getSchedules(Integer userId) {
        return BusinessUtil.convertCourseVOtoScheduleVO(this.getCourses(userId));
    }
}
