package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.ActivityDto;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import ca.uqtr.fitbit.repository.*;
import ca.uqtr.fitbit.service.auth.AuthService;
import javassist.bytecode.stackmap.TypeData;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ActivityServiceImpl implements ActivityService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    private AuthService authService;
    private FitbitApi api;
    private ActivityRepository activityRepository;
    private PatientDeviceRepository patientDeviceRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private DeviceRepository deviceRepository;

    @Autowired
    public ActivityServiceImpl(AuthService authService, FitbitApi api, ActivityRepository activityRepository, PatientDeviceRepository patientDeviceRepository, ModelMapper modelMapper, MessageSource messageSource, DeviceRepository deviceRepository) {
        this.authService = authService;
        this.api = api;
        this.activityRepository = activityRepository;
        this.patientDeviceRepository = patientDeviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public void getDataOfDayPerMinuteFromApi(Stri3ng date, DeviceDto deviceDto) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("steps",date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("calories",date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("distance",date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        this.saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
        this.saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
        this.saveDistanceOfDayFromApiInDB(activitiesDistance, deviceDto);
    }

    @Override
    public void getDataOfDayBetweenTwoTimesPerMinuteFromApi(String date, String endDate, String startTime, String endTime, DeviceDto deviceDto) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("steps",date,endDate,startTime,endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("calories",date,endDate,startTime,endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("distance",date,endDate,startTime,endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        System.out.println("////////////////////activitiesSteps   "+activitiesSteps.getDateTime().toString());
        System.out.println("////////////////////activitiesCalories   "+activitiesCalories.getDateTime().toString());
        System.out.println("////////////////////activitiesDistance   "+activitiesDistance.getDateTime().toString());
        PatientDevice patientDevice = patientDeviceRepository.getPatientDeviceByDeviceId(deviceDto.getId());
        System.out.println("////////////////////   "+patientDevice.toString());

    }

    //------------------------------------------------------------------- 1day 1min
    @Override//----------------------------- steps
    public Response getStepsOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException {
        try {
            ActivitiesSteps activitiesSteps = api.getSteps().getStepsOfDayPerMinuteFromApi(date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            //ActivitiesSteps activitiesSteps = (ActivitiesSteps) api.getActivitiesTypeData().getDataOfDayPerMinute("",date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));

            this.saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
            return new Response(activitiesSteps, null);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public void saveStepsOfDayFromApiInDB(ActivitiesSteps activitiesSteps, DeviceDto deviceDto) {
        System.out.println("...............................   "+deviceDto.toString());
        PatientDevice patientDevice = patientDeviceRepository.getPatientDeviceByDeviceId(deviceDto.getId());
        System.out.println("////////////////////   "+patientDevice.toString());
        patientDevice.getActivitiesSteps().add(activitiesSteps);
        System.out.println("*************  "+ activitiesSteps.toString());
        System.out.println("--------------  "+patientDevice.getActivitiesSteps().toString());
        patientDeviceRepository.save(patientDevice);
       // patientDeviceRepository.save(patientDevice);
        //activitiesStepsRepository.save(activitiesSteps);
    }

    @Override//----------------------------- calories
    public Response getCaloriesOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException {
        try {
            ActivitiesCalories activitiesCalories = api.getCalories().getCaloriesOfDayPerMinute(date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            this.saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
            return new Response(activitiesCalories, null);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
    @Override
    public void saveCaloriesOfDayFromApiInDB(ActivitiesCalories activitiesCalories, DeviceDto deviceDto) {
        Device device = deviceRepository.getDeviceWith_LastPatientDevice_FetchTypeEAGER(deviceDto.getId());
        device.getPatientDevices().get(0).getActivitiesCalories().add(activitiesCalories);
        deviceRepository.save(device);
        //activitiesCaloriesRepository.save(activitiesCalories);
    }

    @Override
    public void saveDistanceOfDayFromApiInDB(ActivitiesDistance activitiesDistance, DeviceDto deviceDto) {
        Device device = deviceRepository.getDeviceWith_LastPatientDevice_FetchTypeEAGER(deviceDto.getId());
        device.getPatientDevices().get(0).getActivitiesDistance().add(activitiesDistance);
        deviceRepository.save(device);
    }

    //*------------------------------------------------------------------------ 1day 2times 1min
    @Override
    public Response getStepsOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException {
        try {
            ActivitiesSteps activitiesSteps = api.getSteps().getStepsOfDayBetweenTwoTimePerMinuteFromApi(date, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            this.saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
            return new Response(activitiesSteps, null);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
    @Override
    public Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException {
        try {
            ActivitiesCalories activitiesCalories = api.getCalories().getCaloriesOfDayBetweenTwoTimePerMinute(date, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            this.saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
            return new Response(activitiesCalories, null);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
//---------------------------------------------------------- not intraday data (!1min)
    @Override
    public Response getActivitiesBetween2DatesFromApi(String date1, String date2, DeviceDto deviceDto) throws IOException, ParseException {
        try {
            List<Activity> activities = api.getActivities().getActivities(date1, date2, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            Type activitiesDto = new TypeToken<List<ActivityDto>>() {}.getType();
            return new Response(modelMapper.map(saveActivitiesBetween2DatesFromApiInDB(activities, deviceDto), activitiesDto), null);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
    @Override
    public Iterable<Activity> saveActivitiesBetween2DatesFromApiInDB(List<Activity> activities, DeviceDto deviceDto) {
        return activityRepository.saveAll(activities);
    }

    @Override
    public Response getStepsOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) throws IOException {
        return null;
    }

    @Override
    public Response getCaloriesOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) throws IOException, ParseException {
        return null;
    }

    @Override
    public Response getStepsOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException {
        return null;
    }

    @Override
    public Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) throws IOException {
        return null;
    }

    @Override
    public Response getActivitiesBetween2DatesFromDB(String date1, String date2, DeviceDto deviceDto) throws IOException, ParseException {
        return null;
    }

}
