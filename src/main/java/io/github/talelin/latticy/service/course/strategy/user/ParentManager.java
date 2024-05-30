package io.github.talelin.latticy.service.course.strategy.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.mapper.course.StudentMapper;
import io.github.talelin.latticy.model.course.Parent;
import io.github.talelin.latticy.model.course.Student;
import io.github.talelin.latticy.service.course.ParentService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ParentManager implements UserManagerStrategy {

    private ParentService parentService;
    private StudentManager studentManager;
    private StudentMapper studentMapper;

    @Override
    public void register(RegisterDTO registerDTO) {
        // there is no registration for parent, but by student manager
    }

    @Override
    public boolean deleteRelation(Integer userId) {
        QueryWrapper<Parent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Parent::getUserId, userId);
        return parentService.remove(queryWrapper);
    }

    @Override
    public List<CourseVO> getCourses(Integer userId) {
        // get his child's courses
        return studentManager.getCourses(getStudentUserIdByParentUserId(userId));
    }

    @Override
    public List<ScheduleDetailVO> getSchedules(Integer userId) {
        return BusinessUtil.convertCourseVOtoScheduleVO(this.getCourses(userId));
    }

    private Integer getStudentUserIdByParentUserId(Integer userId) {
        // get student id by parent user id
        QueryWrapper<Parent> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Parent::getUserId, userId);
        Integer studentId = parentService.getOne(queryWrapper).getStudentId();

        // get student user id by student id
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();
        studentQueryWrapper.lambda().eq(Student::getId, studentId);
        return studentMapper.selectOne(studentQueryWrapper).getUserId();
    }
}
