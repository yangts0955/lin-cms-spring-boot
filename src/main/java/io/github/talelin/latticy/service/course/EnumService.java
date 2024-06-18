package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.course.EnumDO;

public interface EnumService extends IService<EnumDO> {

    String convertEnum(String enumValue);
}
