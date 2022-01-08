package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.view.Steps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActivitiesStepsDto implements Serializable {

    List<Steps> stepsDtoMap = new ArrayList<>();
    private String initDate;

    public ActivitiesStepsDto(List<Steps> stepsDtoMap, String initDate) {
        this.stepsDtoMap = stepsDtoMap;
        this.initDate = initDate;
    }
}
