package ca.uqtr.fitbit.api.data.steps;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;

import java.io.IOException;
import java.util.List;

public interface Steps {

    ActivitiesSteps getStepsOfDayPerMinute(String date, String accessToken) throws IOException;
    ActivitiesSteps getStepsOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime, String accessToken) throws IOException;
}
