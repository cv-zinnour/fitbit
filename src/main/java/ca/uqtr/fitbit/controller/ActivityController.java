package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import ca.uqtr.fitbit.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public ResponseEntity<List<Activity>> getActivitiesBetween2Dates(@RequestBody String twoDates) throws IOException, ParseException {
        List<Activity> activities = activityService.getActivitiesBetween2Dates(twoDates);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @GetMapping(value = "/steps/day/{date}/time/minute/1")
    @ResponseBody
    public ResponseEntity<ActivitiesSteps> getStepsOfDayPerMinute(@PathVariable String date) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = activityService.getStepsOfDayPerMinute(date);
        return new ResponseEntity<>(activitiesSteps, HttpStatus.OK);
    }

    @GetMapping(value = "/steps/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public ResponseEntity<ActivitiesSteps> getStepsOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                                                @PathVariable String startTime,
                                                                                @PathVariable String endTime) throws IOException {
        ActivitiesSteps activitiesSteps = activityService.getStepsOfDayBetweenTwoTimePerMinute(date, startTime, endTime);
        return new ResponseEntity<>(activitiesSteps, HttpStatus.OK);
    }

    @GetMapping(value = "/calories/day/{date}/time/minute/1")
    @ResponseBody
    public ResponseEntity<ActivitiesSteps> getCaloriesOfDayPerMinute(@PathVariable String date) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = activityService.getStepsOfDayPerMinute(date);
        return new ResponseEntity<>(activitiesSteps, HttpStatus.OK);
    }

    @GetMapping(value = "/calories/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public ResponseEntity<ActivitiesCalories> getCaloriesOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                                                   @PathVariable String startTime,
                                                                                   @PathVariable String endTime) throws IOException {
        ActivitiesCalories activitiesCalories = activityService.getCaloriesOfDayBetweenTwoTimePerMinute(date, startTime, endTime);
        return new ResponseEntity<>(activitiesCalories, HttpStatus.OK);
    }

}
