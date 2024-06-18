package io.github.talelin.latticy.vo.course;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EnumVO {

    private List<EnumValueVO> values;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EnumValueVO {
        private Integer type;
        private String name;
        private String value;
    }
}
