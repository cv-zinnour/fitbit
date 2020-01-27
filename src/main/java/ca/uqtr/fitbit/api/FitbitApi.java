package ca.uqtr.fitbit.api;


import ca.uqtr.fitbit.api.data.activities.Activities;
import ca.uqtr.fitbit.api.data.calories.Calories;
import ca.uqtr.fitbit.api.data.steps.Steps;
import ca.uqtr.fitbit.entity.fitbit.Auth;

import java.io.IOException;

public interface FitbitApi {

    String getAccessTokenRefreshToken(String authorizationCode) throws IOException;

    boolean isTokenExpired(String accessToken) throws IOException;

    Auth refreshToken(String refreshToken, Auth auth) throws IOException ;

    Activities getActivities();

    Steps getSteps();

    Calories getCalories();
}
