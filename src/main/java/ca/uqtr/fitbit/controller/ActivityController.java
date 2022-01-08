package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.service.activity.ActivityService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Controller
public class ActivityController {

    private ActivityService activityService;
    private ObjectMapper mapper;

    @Autowired
    public ActivityController(ActivityService activityService, ObjectMapper mapper) {
        this.activityService = activityService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/activity/all")
    @ResponseBody
    public Response getActivitiesBetween2Dates(@RequestParam String date1, @RequestParam String date2,
                                               @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getActivitiesBetween2DatesFromApi(date1, date2, deviceDto);
    }

    @GetMapping(value = "/activity/steps/day/{date}/time/minute/1")
    @ResponseBody
    public Response getStepsOfDayPerMinute(@PathVariable String date, @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getStepsOfDayPerMinuteFromApi(date, deviceDto);
    }

    @GetMapping(value = "/activity/steps/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public Response getStepsOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                         @PathVariable String startTime,
                                                         @PathVariable String endTime, @RequestBody Request request) throws IOException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getStepsOfDayBetweenTwoTimesPerMinuteFromApi(date, startTime, endTime, deviceDto);
    }

    @GetMapping(value = "/activity/calories/day/{date}/time/minute/1")
    @ResponseBody
    public Response getCaloriesOfDayPerMinute(@PathVariable String date, @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getCaloriesOfDayPerMinuteFromApi(date, deviceDto);
    }

    @GetMapping(value = "/activity/calories/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public Response getCaloriesOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                            @PathVariable String startTime,
                                                            @PathVariable String endTime, @RequestBody Request request) throws IOException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(date, startTime, endTime, deviceDto);
    }

   /* @PostMapping(value = "/activity/dates/steps")
    @ResponseBody
    public Response getStepsPerVisits(@RequestParam String medicalFileId,
                                     @RequestBody Request request) throws IOException {
        List<Date> dates = mapper.readValue(String.valueOf(request.getObject()), new TypeReference<List<Date>>(){});
        System.out.println(dates);
        return activityService.getStepsPerVisits(medicalFileId, dates);
    }

    @PostMapping(value = "/activity/dates/activeminutes")
    @ResponseBody
    public Response getActiveMinutesPerVisits(@RequestParam String medicalFileId,
                                       @RequestBody Request request) throws IOException {
        List<Date> dates = mapper.readValue(String.valueOf(request.getObject()), new TypeReference<List<Date>>(){});
        System.out.println(dates);
        return activityService.getActiveMinutesPerVisits(medicalFileId, dates);
    }*/


    @PostMapping(value = "/activity/steps")
    @ResponseBody
    public Response getSteps(@RequestParam String medicalFileId) throws IOException {
        return activityService.getSteps(medicalFileId);
    }

    @PostMapping(value = "/activity/activeminutes")
    @ResponseBody
    public Response getActiveMinutes(@RequestParam String medicalFileId) throws IOException {
        return activityService.getActiveMinutes(medicalFileId);
    }


}
