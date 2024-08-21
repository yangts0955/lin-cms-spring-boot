package io.github.talelin.latticy.controller.v1;

import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.latticy.common.factory.QueryConditionFactory;
import io.github.talelin.latticy.common.util.EnumUtil;
import io.github.talelin.latticy.model.enums.EnumTypeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.model.mapper.UserConvertor;
import io.github.talelin.latticy.service.course.QueryService;
import io.github.talelin.latticy.vo.course.GroupUserSimpleVO;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/root/query")
@PermissionModule(value = "root查询")
@Validated
@AllArgsConstructor
public class RootQueryController {

    private QueryService queryService;
    private QueryConditionFactory factory;
    private UserConvertor userConvertor;
    private EnumUtil enumUtil;

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

    @GroupRequired
    @PermissionMeta(value = "查询用户", module = "query")
    @GetMapping("teacherAndStudent")
    public List<GroupUserSimpleVO> queryGroupedTeachersAndStudents() {
        ArrayList<GroupUserSimpleVO> groupUsers = new ArrayList<>();
        GroupUserSimpleVO students =
                userConvertor.buildGroupUsers(enumUtil.getEnumValueByName(EnumTypeEnum.ROLE.value, RoleEnum.STUDENT.getValue()),
                        queryService.getUserList(RoleEnum.STUDENT, null, null));
        GroupUserSimpleVO teachers =
                userConvertor.buildGroupUsers(enumUtil.getEnumValueByName(EnumTypeEnum.ROLE.value, RoleEnum.TEACHER.getValue()),
                        queryService.getUserList(RoleEnum.TEACHER, null, null));
        groupUsers.add(students);
        groupUsers.add(teachers);
        return groupUsers;
    }
}