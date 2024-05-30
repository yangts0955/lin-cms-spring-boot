package io.github.talelin.latticy.model.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum CourseStatusEnum implements IEnum<String> {

    TO_START,
    IN_PROGRESS,
    FINISHED;

    @Override
    public String getValue() {
        return this.name();
    }
}
