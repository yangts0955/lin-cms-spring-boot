package io.github.talelin.latticy.controller.v1;

import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.latticy.common.factory.QueryConditionFactory;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.course.QueryService;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/root/query")
@PermissionModule(value = "root查询")
@Validated
@AllArgsConstructor
public class RootQueryController {

    private QueryService queryService;
    private QueryConditionFactory factory;

    @GroupRequired
    @PermissionMeta(value = "查询老师", module = "query")
    @GetMapping("teacher")
    public List<UserSimpleVO> queryTeachers(@RequestParam(required = false) String subject) {
        return ObjectUtils.isEmpty(subject) ?
                queryService.getUserList(RoleEnum.TEACHER, null, null) :
                queryService.getUserList(RoleEnum.TEACHER, factory.getSortStrategy("subject"), subject);
    }

    @GroupRequired
    @PermissionMeta(value = "查询学生", module = "query")
    @GetMapping("student")
    public List<UserSimpleVO> queryStudents(@RequestParam(required = false) String grade) {
        return ObjectUtils.isEmpty(grade) ?
                queryService.getUserList(RoleEnum.STUDENT, null, null) :
                queryService.getUserList(RoleEnum.STUDENT, factory.getSortStrategy("grade"), grade);
    }
}