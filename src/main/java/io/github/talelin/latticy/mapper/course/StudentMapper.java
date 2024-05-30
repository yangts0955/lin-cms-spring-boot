package io.github.talelin.latticy.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.talelin.latticy.model.course.Student;
import org.apache.ibatis.annotations.Param;

public interface StudentMapper extends BaseMapper<Student> {

    Student getByUserId(@Param("user_id") Integer userId);
}
