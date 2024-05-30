package io.github.talelin.latticy.service.course.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.mapper.course.ScheduleMapper;
import io.github.talelin.latticy.model.course.Schedule;
import io.github.talelin.latticy.service.course.BatchScheduleService;
import io.github.talelin.latticy.service.course.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class BatchScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements BatchScheduleService {

    private ScheduleService scheduleService;

    @Override
    @Transactional
    public boolean batchCreateSchedule(List<PostScheduleDTO> scheduleDTOs) {
        List<CompletableFuture<Boolean>> futures = scheduleDTOs.stream()
                .map(scheduleDTO -> CompletableFuture.supplyAsync(
                        () -> scheduleService.createSchedule(scheduleDTO))
                ).toList();
        return futures.stream().allMatch(CompletableFuture::join);
    }
}
