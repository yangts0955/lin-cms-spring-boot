package io.github.talelin.latticy.service.course.strategy.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.constant.CommonConstant;
import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.mapper.course.CourseMapper;
import io.github.talelin.latticy.mapper.course.StudentMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.course.Parent;
import io.github.talelin.latticy.model.course.Student;
import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.service.course.ParentService;
import io.github.talelin.latticy.service.course.StudentService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import io.github.talelin.latticy.vo.course.ScheduleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentManager implements UserManagerStrategy {

    private StudentService studentService;
    private ParentService parentService;
    private UserService userService;
    private StudentMapper studentMapper;
    private CourseMapper courseMapper;

    @Transactional
    public void register(RegisterDTO studentRegisterDto) {
        // register student account
        BusinessUtil.assignInnerGroup(studentRegisterDto, InnerGroupEnum.STUDENT);
        UserDO student = userService.createUser(studentRegisterDto);
        GradeEnum grade = CommonUtil.getGradeName(studentRegisterDto.getGrade());
        Student studentInsert = new Student(grade, null, student.getId());
        studentService.save(studentInsert);

        // register parent account automatically with student account
        RegisterDTO parentRegisterDto = createParentCreationDTO(studentRegisterDto);
        BusinessUtil.assignInnerGroup(parentRegisterDto, InnerGroupEnum.PARENT);
        UserDO parent = userService.createUser(parentRegisterDto);
        Parent parentInsert = new Parent(null, parent.getId());
        parentService.save(parentInsert);

        // bind student and parent
        bindChildAndParent(studentInsert, parentInsert);
    }

    @Override
    public boolean deleteRelation(Integer userId) {
        Student student = studentMapper.getByUserId(userId);
        // delete student
        boolean studentRemoved = studentService.removeById(student.getId());
        // delete parent
        Integer parentId = student.getParentId();
        Parent parent = parentService.getById(parentId);
        if (!ObjectUtils.isEmpty(parent)) {
            boolean userRemoved = userService.removeById(parent.getUserId());
            boolean parentRemoved = parentService.removeById(parentId);
            return parentRemoved && studentRemoved && userRemoved;
        }
        return studentRemoved;
    }

    @Override
    public List<CourseVO> getCourses(Integer userId) {
        Integer studentId = getStudentIdByUserId(userId);
        List<CourseVO> courses = courseMapper.queryAllCoursesByStudentId(studentId);
        for (CourseVO course : courses) {
            List<ScheduleVO> scheduleVOS = new ArrayList<>();
            for (ScheduleVO scheduleVO : course.getSchedules()) {
                if (scheduleVO.getStudentIds().contains(studentId)) {
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

    private Integer getStudentIdByUserId(Integer userId) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Student::getUserId, userId);
        return studentService.getOne(queryWrapper).getId();
    }

    private RegisterDTO createParentCreationDTO(RegisterDTO studentInfo) {
        return RegisterDTO.builder()
                .username(studentInfo.getUsername() + CommonConstant.PARENT_USERNAME_POSTFIX)
                .password(studentInfo.getPassword())
                .confirmPassword(studentInfo.getConfirmPassword())
                .role(RoleEnum.PARENT.name())
                .grade(studentInfo.getGrade())
                .build();
    }

    private void bindChildAndParent(Student student, Parent parent) {
        student.setParentId(parent.getId());
        studentService.updateById(student);
        parent.setStudentId(student.getId());
        parentService.saveOrUpdate(parent);
    }
}
