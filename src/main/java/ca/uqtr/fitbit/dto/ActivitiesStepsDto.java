package ca.uqtr.fitbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActivitiesStepsDto implements Serializable {

    private static final long serialVersionUID = 1L;
    Map<String, List<StepsDto>> stepsDtoMap = new HashMap<>();
    private String initDate;

    public ActivitiesStepsDto(Map<String, List<StepsDto>> stepsDtoMap, String initDate) {
        this.stepsDtoMap = stepsDtoMap;
        this.initDate = initDate;
    }
}
