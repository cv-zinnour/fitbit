package ca.uqtr.fitbit.api.data.distance;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DistanceImpl implements Distance {
    @Override
    public ActivitiesDistance getDistanceOfDayPerMinute(String date, String accessToken) throws IOException {
        return null;
    }

    @Override
    public ActivitiesDistance getDistanceOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime, String accessToken) throws IOException {
        return null;
    }
}
