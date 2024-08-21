package io.github.talelin.latticy.vo.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupUserSimpleVO {
    String role;
    List<UserSimpleVO> users;
}
