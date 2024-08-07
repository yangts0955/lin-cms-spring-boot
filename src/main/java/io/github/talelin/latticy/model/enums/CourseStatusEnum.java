package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum CourseStatusEnum implements IEnum<String> {

    IN_PROGRESS,
    TO_START,
    FINISHED;

    @Override
    public String getValue() {
        return this.name();
    }
}
