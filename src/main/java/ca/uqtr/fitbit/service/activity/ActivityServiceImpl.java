package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import ca.uqtr.fitbit.repository.ActivitiesStepsRepository;
import ca.uqtr.fitbit.repository.ActivityRepository;
import ca.uqtr.fitbit.service.auth.AuthService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    private AuthService authService;
    private FitbitApi api;
    private ActivityRepository activityRepository;
    private final ActivitiesStepsRepository activitiesStepsRepository;

    @Autowired
    public ActivityServiceImpl(AuthService authService, FitbitApi api, ActivityRepository activityRepository, ActivitiesStepsRepository activitiesStepsRepository) {
        this.authService = authService;
        this.api = api;
        this.activityRepository = activityRepository;
        this.activitiesStepsRepository = activitiesStepsRepository;
    }

    @Override
    public List<Activity> getActivitiesBetween2Dates(String twoDates) throws IOException, ParseException {
        JSONObject jsonObj = new JSONObject(twoDates);
        List<Activity> activities = api.getActivities().getActivities(jsonObj.getString("dateStart"), jsonObj.getString("dateEnd"), authService.getAccessToken());
        this.saveActivitiesBetween2Dates(activities);
        return activities;
    }

    @Override
    public void saveActivitiesBetween2Dates(List<Activity> activities) {
        activityRepository.saveAll(activities);
    }

    @Override
    public ActivitiesSteps getStepsOfDayPerMinute(String date) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = api.getSteps().getStepsOfDayPerMinute(date, authService.getAccessToken());
        this.saveStepsOfDayPerMinute(activitiesSteps);
        return activitiesSteps;
    }

    @Override
    public ActivitiesSteps getStepsOfDayBetweenTwoTimePerMinute(String date, String startTime, String endTime) throws IOException {
        ActivitiesSteps activitiesSteps = api.getSteps().getStepsOfDayBetweenTwoTimePerMinute(date, startTime, endTime, authService.getAccessToken());
        this.saveStepsOfDayPerMinute(activitiesSteps);
        return activitiesSteps;
    }

    @Override
    public void saveStepsOfDayPerMinute(ActivitiesSteps activitiesSteps) {
        activitiesStepsRepository.save(activitiesSteps);
    }

}
