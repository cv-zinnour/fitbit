package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface ActivityService{

    void getDataOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException;
    void getDataOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException;



    Response getStepsOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException;
    void saveStepsOfDayFromApiInDB(ActivitiesSteps activitiesSteps, DeviceDto deviceDto);
    Response getCaloriesOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException, ParseException;
    void saveCaloriesOfDayFromApiInDB(ActivitiesCalories activitiesCalories, DeviceDto deviceDto);
    void saveDistanceOfDayFromApiInDB(ActivitiesDistance activitiesDistance, DeviceDto deviceDto);


    Response getStepsOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException;
    Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException;
    Response getActivitiesBetween2DatesFromApi(String date1, String date2, DeviceDto deviceDto) throws IOException, ParseException;
    Iterable<Activity> saveActivitiesBetween2DatesFromApiInDB(List<Activity> activities, DeviceDto deviceDto);


    Response getStepsOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) throws IOException;
    Response getCaloriesOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) throws IOException, ParseException;

    Response getStepsOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException;
    Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException;
    Response getActivitiesBetween2DatesFromDB(String date1, String date2, DeviceDto deviceDto) throws IOException, ParseException;

}

