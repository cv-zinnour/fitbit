package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import ca.uqtr.fitbit.service.activity.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Value("${fitbit.subscription.verification-code}")
    private String fitbitVerificationCode;
    private ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public Response getActivitiesBetween2Dates(@RequestParam String date1, @RequestParam String date2,
                                               @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getActivitiesBetween2DatesFromApi(date1, date2, deviceDto);
    }

    @GetMapping(value = "/steps/day/{date}/time/minute/1")
    @ResponseBody
    public Response getStepsOfDayPerMinute(@PathVariable String date, @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getStepsOfDayPerMinuteFromApi(date, deviceDto);
    }

    @GetMapping(value = "/steps/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public Response getStepsOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                                                @PathVariable String startTime,
                                                                                @PathVariable String endTime, @RequestBody Request request) throws IOException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getStepsOfDayBetweenTwoTimesPerMinuteFromApi(date, startTime, endTime, deviceDto);
    }

    @GetMapping(value = "/calories/day/{date}/time/minute/1")
    @ResponseBody
    public Response getCaloriesOfDayPerMinute(@PathVariable String date, @RequestBody Request request) throws IOException, ParseException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getCaloriesOfDayPerMinuteFromApi(date, deviceDto);
    }

    @GetMapping(value = "/calories/day/{date}/time/{startTime}/{endTime}/minute/1")
    @ResponseBody
    public Response getCaloriesOfDayBetweenTwoTimePerMinute(@PathVariable String date,
                                                                                   @PathVariable String startTime,
                                                                                   @PathVariable String endTime, @RequestBody Request request) throws IOException {
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        return activityService.getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(date, startTime, endTime, deviceDto);
    }

    @GetMapping("/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotification(@RequestParam("verify") String verify) {
        System.out.println("----------------------------------- " + verify);
        if(verify.equals(fitbitVerificationCode)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/subscription/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotificationData(@PathParam("version") String version,List notificationList) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //Runnable taskOne = new FitbitDataThread(notificationList);
        //executor.execute(taskOne);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
