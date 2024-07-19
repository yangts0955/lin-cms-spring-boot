package io.github.talelin.latticy.controller.v1;

import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.factory.UserManagerFactory;
import io.github.talelin.latticy.common.util.EnumUtil;
import io.github.talelin.latticy.dto.course.PostCourseDTO;
import io.github.talelin.latticy.dto.course.PutCourseDTO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.enums.EnumTypeEnum;
import io.github.talelin.latticy.service.course.CourseService;
import io.github.talelin.latticy.service.course.strategy.user.UserManagerStrategy;
import io.github.talelin.latticy.vo.CreatedVO;
import io.github.talelin.latticy.vo.DeletedVO;
import io.github.talelin.latticy.vo.UpdatedVO;
import io.github.talelin.latticy.vo.course.CourseVO;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/course")
@PermissionModule(value = "课程")
@Validated
@AllArgsConstructor
public class CourseController {

    private CourseService courseService;
    private UserManagerFactory userManagerFactory;
    private EnumUtil enumUtil;

    @PostMapping
    @GroupRequired
    @PermissionMeta(value = "创建课程", module = "课程")
    public CreatedVO createCourse(@RequestBody PostCourseDTO courseDTO) {
        boolean success = courseService.createCourse(courseDTO);
        if (!success) {
            throw new FailedException(10200, "创建课程失败");
        }
        return new CreatedVO(1);
    }

    @DeleteMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "删除课程", module = "课程")
    public DeletedVO deleteCourse(@PathVariable("id") Integer courseId) {
        courseService.deleteCourse(courseId);
        return new DeletedVO(3);
    }

    @PutMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "更新课程", module = "课程")
    public UpdatedVO updateCourse(@PathVariable("id") Integer courseId, @RequestBody PutCourseDTO courseDTO) {
        courseService.updateCourse(courseId, courseDTO);
        return new UpdatedVO(2);
    }

    @GetMapping
    @GroupRequired
    @PermissionMeta(value = "获取课程", module = "课程")
    public List<CourseVO> getAllCourses() {
        UserDO user = LocalUser.getLocalUser();
        UserManagerStrategy strategy = userManagerFactory.getUserStrategy(user.getRole());
        List<CourseVO> courses = strategy.getCourses(user.getId());
        courses.forEach(courseVO -> {
            courseVO.setGradeName(enumUtil.getEnumValueByName(EnumTypeEnum.GRADE.value, courseVO.getGrade().getValue()));
            courseVO.setSubjectName(enumUtil.getEnumValueByName(EnumTypeEnum.SUBJECT.value, courseVO.getSubject().getValue()));
        });
        return courses;
    }

    @GetMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "获取课程详情", module = "课程")
    public CourseVO getCourse(@PathVariable Integer id) {
        return courseService.getCourseById(id);
    }
}
