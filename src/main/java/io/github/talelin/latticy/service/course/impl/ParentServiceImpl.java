package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.ParentMapper;
import io.github.talelin.latticy.model.course.Parent;
import io.github.talelin.latticy.service.course.ParentService;
import org.springframework.stereotype.Service;

@Service
public class ParentServiceImpl extends ServiceImpl<ParentMapper, Parent> implements ParentService {
}
