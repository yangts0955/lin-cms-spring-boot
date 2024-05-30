package io.github.talelin.latticy.service.course.strategy.user;

import io.github.talelin.latticy.common.util.BusinessUtil;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.model.enums.InnerGroupEnum;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.vo.course.CourseVO;
import io.github.talelin.latticy.vo.course.ScheduleDetailVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class GuestManager implements UserManagerStrategy {

    private UserService userService;

    @Override
    public void register(RegisterDTO registerDTO) {
        BusinessUtil.assignInnerGroup(registerDTO, InnerGroupEnum.GUEST);
        userService.createUser(registerDTO);
    }

    @Override
    public boolean deleteRelation(Integer userId) {
        // no operation
        return true;
    }

    @Override
    public List<CourseVO> getCourses(Integer userId) {
        return new ArrayList<>();
    }

    @Override
    public List<ScheduleDetailVO> getSchedules(Integer userId) {
        return new ArrayList<>();
    }
}
