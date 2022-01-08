package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.FitbitAPIException;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

public interface ActivityService{

    void getDataOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException, ParseException, FitbitAPIException;
    void getDataOfDayBetweenTwoTimesPerMinuteFromApi(String date, String endDate, String startTime, String endTime, Timestamp t1, Timestamp t2, Timestamp syncTime, DeviceDto deviceDto) throws FitbitAPIException, IOException, ParseException;


    Response getStepsOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) ;
    void saveStepsOfDayFromApiInDB(ActivitiesSteps activitiesSteps, DeviceDto deviceDto);
    Response getCaloriesOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) ;
    void saveCaloriesOfDayFromApiInDB(ActivitiesCalories activitiesCalories, DeviceDto deviceDto);
    void saveDistanceOfDayFromApiInDB(ActivitiesDistance activitiesDistance, DeviceDto deviceDto);


    Response getStepsOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) ;
    Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto);
    Response getActivitiesBetween2DatesFromApi(String date1, String date2, DeviceDto deviceDto);
    Iterable<Activity> saveActivitiesBetween2DatesFromApiInDB(List<Activity> activities, DeviceDto deviceDto);


    Response getStepsOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) ;
    Response getCaloriesOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) ;

    Response getStepsOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) ;
    Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) ;
    Response getActivitiesBetween2DatesFromDB(String date1, String date2, DeviceDto deviceDto) ;

    Response getStepsPerVisits(String medicalFileId, List<Date> dates);
    Response getActiveMinutesPerVisits(String medicalFileId, List<Date> dates);

    Response getSteps(String medicalFileId);
    Response getActiveMinutes(String medicalFileId);

}

