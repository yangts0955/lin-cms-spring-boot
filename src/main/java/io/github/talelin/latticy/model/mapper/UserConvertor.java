package io.github.talelin.latticy.model.mapper;

import io.github.talelin.latticy.vo.course.GroupUserSimpleVO;
import io.github.talelin.latticy.vo.course.UserSimpleVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class UserConvertor {

    public GroupUserSimpleVO buildGroupUsers(String role, List<UserSimpleVO> users) {
        return GroupUserSimpleVO.builder()
                .role(role)
                .users(users)
                .build();
    }
}
