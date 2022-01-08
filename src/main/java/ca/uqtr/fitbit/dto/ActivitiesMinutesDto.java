package ca.uqtr.fitbit.dto;

import ca.uqtr.fitbit.entity.view.Minutes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActivitiesMinutesDto implements Serializable {

    List<Minutes> minutesDtoMap = new ArrayList<>();
    private String initDate;

    public ActivitiesMinutesDto(List<Minutes> minutesDtoMap, String initDate) {
        this.minutesDtoMap = minutesDtoMap;
        this.initDate = initDate;
    }
}
