package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.model.course.Schedule;

import java.util.List;

public interface BatchScheduleService extends IService<Schedule> {


    boolean batchCreateSchedule(List<PostScheduleDTO> scheduleDTOs);

}
