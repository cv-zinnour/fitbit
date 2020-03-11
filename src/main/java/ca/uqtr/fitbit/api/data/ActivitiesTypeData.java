package ca.uqtr.fitbit.api.data;

import java.io.IOException;
import java.text.ParseException;

public interface ActivitiesTypeData<T> {

    T getDataOfDayPerMinute(String activityType, String date, String accessToken) throws IOException, ParseException;
    T getDataOfDayBetweenTwoTimePerMinute(String activityType, String date, String endDate, String startTime, String endTime, String accessToken) throws IOException, ParseException;
    Serialization deserialization(String json, String activityType);


}
