package ca.uqtr.fitbit.api.data.calories;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;

import java.io.IOException;
import java.text.ParseException;

public interface Calories {
    ActivitiesCalories getCaloriesOfDayPerMinute(String date, String accessToken) throws IOException, ParseException;
    ActivitiesCalories getCaloriesOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime, String accessToken) throws IOException, ParseException;

}
