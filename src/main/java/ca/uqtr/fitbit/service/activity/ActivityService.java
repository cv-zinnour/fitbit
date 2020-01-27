package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.List;

public interface ActivityService{

    List<Activity> getActivitiesBetween2Dates(String twoDates) throws IOException, ParseException;

    void saveActivitiesBetween2Dates(List<Activity> activities);

    ActivitiesSteps getStepsOfDayPerMinute(String date) throws IOException, ParseException;

    ActivitiesSteps getStepsOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime) throws IOException;

    void saveStepsOfDayPerMinute(ActivitiesSteps activitiesSteps);
}
