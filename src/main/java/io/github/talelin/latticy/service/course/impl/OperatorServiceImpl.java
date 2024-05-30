package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.mapper.course.OperatorMapper;
import io.github.talelin.latticy.model.course.Operator;
import io.github.talelin.latticy.service.course.OperatorService;
import org.springframework.stereotype.Service;

@Service
public class OperatorServiceImpl extends ServiceImpl<OperatorMapper, Operator> implements OperatorService {
}
