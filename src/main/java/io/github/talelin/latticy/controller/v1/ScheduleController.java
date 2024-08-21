package io.github.talelin.latticy.controller.v1;

import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.core.annotation.GroupRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.factory.UserManagerFactory;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.dto.course.PutScheduleDTO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.course.ScheduleService;
import io.github.talelin.latticy.service.course.strategy.user.UserManagerStrategy;
import io.github.talelin.latticy.vo.CreatedVO;
import io.github.talelin.latticy.vo.DeletedVO;
import io.github.talelin.latticy.vo.UpdatedVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/schedule")
@PermissionModule(value = "日程")
@Validated
@AllArgsConstructor
public class ScheduleController {

    private ScheduleService scheduleService;
    private UserManagerFactory userManagerFactory;

    @PostMapping
    @GroupRequired
    @PermissionMeta(value = "创建日程", module = "日程")
    public CreatedVO createSchedule(@RequestBody PostScheduleDTO scheduleDTO) {
        boolean success = scheduleService.createSchedule(scheduleDTO);
        if (!success) {
            throw new FailedException(10200, "创建日程失败");
        }
        return new CreatedVO(1);
    }

    @DeleteMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "删除日程", module = "日程")
    public DeletedVO deleteSchedule(@PathVariable("id") Integer scheduleId) {
        scheduleService.deleteScheduleById(scheduleId);
        return new DeletedVO(3);
    }

    @PutMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "更新日程", module = "日程")
    public UpdatedVO updateSchedule(@PathVariable("id") Integer scheduleId, @RequestBody PutScheduleDTO scheduleDTO) {
        scheduleService.updateSchedule(scheduleId, scheduleDTO);
        return new UpdatedVO(2);
    }

    @GetMapping
    @GroupRequired
    @PermissionMeta(value = "获取日程", module = "日程")
    public List<ScheduleDetailVO> getSchedules() {
        UserDO user = LocalUser.getLocalUser();
        UserManagerStrategy strategy = userManagerFactory.getUserStrategy(user.getRole());
        return strategy.getSchedules(user.getId());
    }

    @GetMapping("{id}")
    @GroupRequired
    @PermissionMeta(value = "获取日程", module = "日程")
    public ScheduleDetailVO getSchedule(@PathVariable(value = "id") Integer scheduleId) {
        UserDO user = LocalUser.getLocalUser();
        UserManagerStrategy strategy = userManagerFactory.getUserStrategy(user.getRole());
        List<ScheduleDetailVO> schedules = strategy.getSchedules(user.getId());
        List<ScheduleDetailVO> list = schedules.stream().filter(schedule -> schedule.getScheduleId().equals(scheduleId)).toList();
        if (!ObjectUtils.isEmpty(list)) {
            return list.get(0);
        }
        throw new FailedException("您没有权限查看该日程");
    }
}
