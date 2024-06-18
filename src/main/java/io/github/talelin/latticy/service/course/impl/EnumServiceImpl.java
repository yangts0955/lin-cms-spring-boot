package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.EnumMapper;
import io.github.talelin.latticy.model.course.EnumDO;
import io.github.talelin.latticy.service.course.EnumService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class EnumServiceImpl extends ServiceImpl<EnumMapper, EnumDO> implements EnumService {

    @Override
    public String convertEnum(String enumValue) {
        QueryWrapper<EnumDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EnumDO::getName, enumValue);
        EnumDO enumDO = this.baseMapper.selectList(queryWrapper).get(0);
        return ObjectUtils.isEmpty(enumDO) ? "" : enumDO.getValue();
    }
}
