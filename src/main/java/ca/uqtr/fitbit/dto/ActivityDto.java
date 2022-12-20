package ca.uqtr.fitbit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Date dateTime;
    private int calories;
    private int steps;
    private double distance;
    private int minutesSedentary;
    private int minutesLightlyActive;
    private int minutesFairlyActive;
    private int minutesVeryActive;
    private int activityCalories;
}
