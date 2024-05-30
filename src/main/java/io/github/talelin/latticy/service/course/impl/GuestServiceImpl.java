package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.GuestMapper;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.course.GuestService;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceImpl extends ServiceImpl<GuestMapper, UserDO> implements GuestService {
}
