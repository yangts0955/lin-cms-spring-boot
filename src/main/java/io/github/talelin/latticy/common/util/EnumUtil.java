package io.github.talelin.latticy.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.mapper.course.EnumMapper;
import io.github.talelin.latticy.model.course.EnumDO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@AllArgsConstructor
public class EnumUtil {

    private EnumMapper enumMapper;

    public String getEnumValueByName(Integer type, String name) {
        QueryWrapper<EnumDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(EnumDO::getName, name)
                .eq(EnumDO::getType, type);
        EnumDO enums = enumMapper.selectOne(queryWrapper);
        return ObjectUtils.isEmpty(enums) ? "" : enums.getValue();
    }
}
