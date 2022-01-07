package ca.uqtr.fitbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ActivitiesMinutesDto implements Serializable {

    private static final long serialVersionUID = 1L;
    Map<String, List<MinutesDto>> minutesDtoMap = new HashMap<>();
    private String initDate;

    public ActivitiesMinutesDto(Map<String, List<MinutesDto>> minutesDtoMap, String initDate) {
        this.minutesDtoMap = minutesDtoMap;
        this.initDate = initDate;
    }
}
