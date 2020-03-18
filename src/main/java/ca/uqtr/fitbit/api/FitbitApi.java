package ca.uqtr.fitbit.api;


import ca.uqtr.fitbit.api.data.ActivitiesTypeData;
import ca.uqtr.fitbit.api.data.ActivitiesTypeDataImpl;
import ca.uqtr.fitbit.api.data.activities.Activities;
import ca.uqtr.fitbit.api.data.calories.Calories;
import ca.uqtr.fitbit.api.data.distance.Distance;
import ca.uqtr.fitbit.api.data.steps.Steps;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.fitbit.Auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface FitbitApi {

    String getAccessTokenRefreshToken(String authorizationCode) throws IOException;

    boolean isTokenExpired(String accessToken) throws IOException;

    Auth refreshToken(String refreshToken, Auth auth) throws IOException ;

    Response updateProfile(String accessToken, String gender, String birthday, String height);

    Response updateWeight(String accessToken, String weight, String date, String time) throws UnsupportedEncodingException;

    Response addSubscription(FitbitSubscription fitbitSubscription, String accessToken, String collectionPath) throws IOException;

    Response removeSubscription(FitbitSubscription fitbitSubscription, String accessToken, String collectionPath) throws IOException;

    Response allSubscriptions(String accessToken, String collectionPath) throws IOException;

    Activities getActivities();

    Steps getSteps();

    Calories getCalories();

    Distance geDistance();

    ActivitiesTypeData getActivitiesTypeData();

    String getFitbitProfileId(String accessToken);
}
