package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

@Getter
public enum InnerGroupEnum implements IEnum<Integer> {

    ROOT(1),
    TEACHER(2),
    STUDENT(3),
    PARENT(4),
    OPERATOR(5),
    GUEST(6);

    private final Integer groupId;

    InnerGroupEnum(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public Integer getValue() {
        return this.getGroupId();
    }
}
