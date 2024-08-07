package io.github.talelin.latticy.service.impl;

import static io.github.talelin.latticy.common.util.CommonUtil.calculateGradeSignal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.common.factory.UserManagerFactory;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.common.util.CommonUtil;
import io.github.talelin.latticy.dto.admin.*;
import io.github.talelin.latticy.mapper.GroupPermissionMapper;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.mapper.course.TeacherMapper;
import io.github.talelin.latticy.model.*;
import io.github.talelin.latticy.model.course.Student;
import io.github.talelin.latticy.model.course.Teacher;
import io.github.talelin.latticy.model.enums.GradeEnum;
import io.github.talelin.latticy.model.enums.RoleEnum;
import io.github.talelin.latticy.service.*;
import io.github.talelin.latticy.service.course.StudentService;
import io.github.talelin.latticy.service.course.strategy.user.UserManagerStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pedro@TaleLin
 * @author colorful@TaleLin
 * @author Juzi@TaleLin
 * 管理员服务实现类
 */
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private UserService userService;
    private UserIdentityService userIdentityService;
    private GroupService groupService;
    private PermissionService permissionService;
    private GroupPermissionMapper groupPermissionMapper;
    private UserGroupMapper userGroupMapper;
    private TeacherMapper teacherMapper;
    private StudentService studentService;
    private UserManagerFactory userManagerFactory;

    @Override
    public IPage<UserDO> getUserPageByGroupId(Integer groupId, Integer count, Integer page) {
        LinPage<UserDO> pager = new LinPage<>(page, count);
        IPage<UserDO> iPage;
        // 如果group_id为空，则以分页的形式返回所有用户
        if (groupId == null) {
            QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
            Integer rootUserId = userService.getRootUserId();
            wrapper.lambda().ne(UserDO::getId, rootUserId);
            iPage = userService.page(pager, wrapper);
        } else {
            iPage = userService.getUserPageByGroupId(pager, groupId);
        }
        return iPage;
    }

    @Override
    public boolean changeUserPassword(Integer id, ResetPasswordDTO dto) {
        throwUserNotExistById(id);
        return userIdentityService.changePassword(id, dto.getNewPassword());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteUser(Integer id) {
        throwUserNotExistById(id);
        if (userService.getRootUserId().equals(id)) {
            throw new ForbiddenException(10079);
        }
        QueryWrapper<UserIdentityDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserIdentityDO::getUserId, id);
        // 删除用户，还应当将 user_group表中的数据删除
        boolean deleteResult = userGroupMapper.deleteByUserId(id) > 0;
        boolean userIdentityRemoved = userIdentityService.remove(wrapper);
        // 删除用户角色关联表数据
        UserManagerStrategy userStrategy = userManagerFactory.getUserStrategy(userService.getById(id).getRole());
        boolean relationDeleted = userStrategy.deleteRelation(id);
        boolean userRemoved = userService.removeById(id);
        return userRemoved && userIdentityRemoved && deleteResult && relationDeleted;
    }

    @Override
    public boolean updateUserInfo(Integer id, UpdateUserInfoDTO validator) {
        List<Integer> newGroupIds = validator.getGroupIds();
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        boolean anyMatch = newGroupIds.stream().anyMatch(it -> it.equals(rootGroupId));
        if (anyMatch) {
            throw new ForbiddenException(10073);
        }
        List<Integer> existGroupIds = groupService.getUserGroupIdsByUserId(id);
        // 删除existGroupIds有，而newGroupIds没有的
        List<Integer> deleteIds = existGroupIds.stream().filter(it -> !newGroupIds.contains(it)).toList();
        // 添加newGroupIds有，而existGroupIds没有的
        List<Integer> addIds = newGroupIds.stream().filter(it -> !existGroupIds.contains(it)).toList();
        updateUserBaseInfo(id, validator);
        return groupService.deleteUserGroupRelations(id, deleteIds) && groupService.addUserGroupRelations(id, addIds);
    }

    @Override
    public IPage<GroupDO> getGroupPage(Integer page, Integer count) {
        return groupService.getGroupPage(page, count);
    }

    @Override
    public GroupPermissionBO getGroup(Integer id) {
        throwGroupNotExistById(id);
        return groupService.getGroupAndPermissions(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean createGroup(NewGroupDTO dto) {
        throwGroupNameExist(dto.getName());
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        groupService.save(group);
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                    .map(id -> new GroupPermissionDO(group.getId(), id))
                    .collect(Collectors.toList());
            groupPermissionMapper.insertBatch(relations);
        }
        return true;
    }

    @Override
    public boolean updateGroup(Integer id, UpdateGroupDTO dto) {
        // bug 如果只修改info，不修改name，则name已经存在，此时不应该报错
        GroupDO exist = groupService.getById(id);
        if (exist == null) {
            throw new NotFoundException(10024);
        }
        if (!exist.getName().equals(dto.getName())) {
            throwGroupNameExist(dto.getName());
        }
        GroupDO group = GroupDO.builder().name(dto.getName()).info(dto.getInfo()).build();
        group.setId(id);
        return groupService.updateById(group);
    }

    @Override
    public boolean deleteGroup(Integer id) {
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        Integer guestGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
        if (id.equals(rootGroupId)) {
            throw new ForbiddenException(10074);
        }
        if (id.equals(guestGroupId)) {
            throw new ForbiddenException(10075);
        }
        throwGroupNotExistById(id);
        List<Integer> groupUserIds = groupService.getGroupUserIds(id);
        if(!groupUserIds.isEmpty()) {
            throw new ForbiddenException(10027);
        }
        return groupService.removeById(id);
    }

    @Override
    public boolean dispatchPermission(DispatchPermissionDTO dto) {
        GroupPermissionDO groupPermission = new GroupPermissionDO(dto.getGroupId(), dto.getPermissionId());
        return groupPermissionMapper.insert(groupPermission) > 0;
    }

    @Override
    public boolean dispatchPermissions(DispatchPermissionsDTO dto) {
        List<GroupPermissionDO> relations = dto.getPermissionIds().stream()
                .map(id -> new GroupPermissionDO(dto.getGroupId(), id))
                .collect(Collectors.toList());
        return groupPermissionMapper.insertBatch(relations) > 0;
    }

    @Override
    public boolean removePermissions(RemovePermissionsDTO dto) {
        return groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(dto.getGroupId(), dto.getPermissionIds()) > 0;
    }

    @Override
    public List<GroupDO> getAllGroups() {
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
        wrapper.lambda().ne(GroupDO::getId, rootGroupId);
        return groupService.list(wrapper);
    }

    @Override
    public List<PermissionDO> getAllPermissions() {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getMount, true);
        return permissionService.list(wrapper);
    }

    @Override
    public Map<String, List<PermissionDO>> getAllStructuralPermissions() {
        QueryWrapper<PermissionDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PermissionDO::getMount, true);
        List<PermissionDO> permissions = getAllPermissions();
        Map<String, List<PermissionDO>> res = new HashMap<>();
        permissions.forEach(permission -> {
            if (res.containsKey(permission.getModule())) {
                res.get(permission.getModule()).add(permission);
            } else {
                ArrayList<PermissionDO> t = new ArrayList<>();
                t.add(permission);
                res.put(permission.getModule(), t);
            }
        });
        return res;
    }

    private void throwUserNotExistById(Integer id) {
        boolean exist = userService.checkUserExistById(id);
        if (!exist) {
            throw new NotFoundException(10021);
        }
    }

    private void throwGroupNotExistById(Integer id) {
        boolean exist = groupService.checkGroupExistById(id);
        if (!exist) {
            throw new NotFoundException(10024);
        }
    }

    private void throwGroupNameExist(String name) {
        boolean exist = groupService.checkGroupExistByName(name);
        if (exist) {
            throw new ForbiddenException(10072);
        }
    }

    private void updateUserBaseInfo(Integer id, UpdateUserInfoDTO validator) {
        if (validateUpdateUserBaseInfo(validator)) {
            return;
        }
        UserDO userDO = userService.getById(id);
        GradeEnum newGrade = CommonUtil.getGradeName(validator.getGrade());
        UserDO user = UserDO.builder()
                .gender(CommonUtil.getGenderName(validator.getGender()))
                .grade(newGrade)
                .subject(CommonUtil.getSubjectName(validator.getSubject()))
                .birthday(validator.getBirthday())
                .realName(validator.getRealName())
                .phoneNumber(validator.getPhoneNumber())
                .wxNumber(validator.getWxNumber())
                .gradeSignal(calculateGradeSignal(userDO.getEntranceDate(), userDO.getGrade(), newGrade, userDO.getGradeSignal()))
                .remark(validator.getRemark())
                .build();
        LambdaUpdateWrapper<UserDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserDO::getId, id);
        userService.update(user, updateWrapper);
        if (!ObjectUtils.isEmpty(validator.getGrade()) && RoleEnum.STUDENT.equals(userDO.getRole())) {
            UpdateWrapper<Student> wrapper = new UpdateWrapper<>();
            wrapper.lambda()
                    .eq(Student::getUserId, id)
                    .set(Student::getGrade, CommonUtil.getGradeName(validator.getGrade()));
            studentService.update(null, wrapper);
        }
        if (!ObjectUtils.isEmpty(validator.getSubject()) && RoleEnum.TEACHER.equals(userDO.getRole())) {
            UpdateWrapper<Teacher> wrapper = new UpdateWrapper<>();
            wrapper.lambda()
                    .eq(Teacher::getUserId, id)
                    .set(Teacher::getSubject, CommonUtil.getSubjectName(validator.getSubject()));
            teacherMapper.update(null, wrapper);
        }
    }

    private boolean validateUpdateUserBaseInfo(UpdateUserInfoDTO userInfoDTO) {
        return ObjectUtils.isEmpty(userInfoDTO.getGrade()) &&
                ObjectUtils.isEmpty(userInfoDTO.getGender()) &&
                ObjectUtils.isEmpty(userInfoDTO.getBirthday()) &&
                ObjectUtils.isEmpty(userInfoDTO.getRealName()) &&
                ObjectUtils.isEmpty(userInfoDTO.getPhoneNumber()) &&
                ObjectUtils.isEmpty(userInfoDTO.getWxNumber()) &&
                ObjectUtils.isEmpty(userInfoDTO.getRemark());
    }

}
