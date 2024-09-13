package io.github.talelin.latticy.service.course;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.dto.course.PostScheduleDTO;
import io.github.talelin.latticy.dto.course.PutScheduleDTO;
import io.github.talelin.latticy.model.course.Schedule;
import io.github.talelin.latticy.vo.course.ScheduleCreateVO;

import java.util.List;

public interface ScheduleService extends IService<Schedule> {

    ScheduleCreateVO createSingleScheduleBeingTiedToCourse(PostScheduleDTO scheduleDTO);

    boolean createSchedule(PostScheduleDTO scheduleDTO);

    void deleteScheduleByCourseId(Integer courseId);

    void deleteScheduleById(Integer scheduleId);

    void updateSchedule(Integer scheduleId, PutScheduleDTO scheduleDTO);

    boolean batchUpdateSchedulesStatus(List<Integer> scheduleIds);
}
