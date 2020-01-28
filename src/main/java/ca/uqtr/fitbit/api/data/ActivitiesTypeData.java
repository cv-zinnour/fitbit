package ca.uqtr.fitbit.api.data;

import java.io.IOException;

public interface ActivitiesTypeData<T> {

    T getDataOfDayPerMinute(String activityType, String date, String accessToken) throws IOException;
    T getDataOfDayBetweenTwoTimePerMinute(String activityType, String date, String startTime, String endTime, String accessToken) throws IOException;
    ActivitiesTypeDataImpl.Serialization deserialization(String json, String activityType);
}
