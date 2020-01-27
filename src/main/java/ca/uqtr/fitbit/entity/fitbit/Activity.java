package ca.uqtr.fitbit.entity.fitbit;

import ca.uqtr.fitbit.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "activity", schema = "public")
public class Activity extends BaseEntity {


    @Column(name = "datetime")
    private Date dateTime;
    @Column(name = "calories")
    private int calories;
    @Column(name = "steps")
    private int steps;
    @Column(name = "distance")
    private double distance;
    @Column(name = "minutes_sedentary")
    private int minutesSedentary;
    @Column(name = "minutes_lightly_active")
    private int minutesLightlyActive;
    @Column(name = "minutes_fairly_active")
    private int minutesFairlyActive;
    @Column(name = "minutes_very_active")
    private int minutesVeryActive;
    @Column(name = "activity_calories")
    private int activityCalories;

    public Activity(Date dateTime, int calories, int steps, double distance, int minutesSedentary, int minutesLightlyActive, int minutesFairlyActive, int minutesVeryActive, int activityCalories) {
        this.dateTime = dateTime;
        this.calories = calories;
        this.steps = steps;
        this.distance = distance;
        this.minutesSedentary = minutesSedentary;
        this.minutesLightlyActive = minutesLightlyActive;
        this.minutesFairlyActive = minutesFairlyActive;
        this.minutesVeryActive = minutesVeryActive;
        this.activityCalories = activityCalories;
    }

    public Activity() {
    }

}
