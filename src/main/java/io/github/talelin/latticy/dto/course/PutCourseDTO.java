package io.github.talelin.latticy.dto.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PutCourseDTO {

    private String name;
    private String subject;
    private String grade;
    private String remark;
}
