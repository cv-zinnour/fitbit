package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.FitbitAPIException;
import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.*;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesCalories;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesDistance;
import ca.uqtr.fitbit.entity.fitbit.ActivitiesSteps;
import ca.uqtr.fitbit.entity.fitbit.Activity;
import ca.uqtr.fitbit.entity.view.Minutes;
import ca.uqtr.fitbit.entity.view.Steps;
import ca.uqtr.fitbit.repository.*;
import ca.uqtr.fitbit.service.auth.AuthService;
import javassist.bytecode.stackmap.TypeData;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
public class ActivityServiceImpl implements ActivityService {
    private static final Logger LOGGER = Logger.getLogger(TypeData.ClassName.class.getName());

    private AuthService authService;
    private FitbitApi api;
    private ActivityRepository activityRepository;
    private PatientDeviceRepository patientDeviceRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private DeviceRepository deviceRepository;
    private StepsRepository stepsRepository;
    private MinutesRepository minutesRepository;

    @Autowired
    public ActivityServiceImpl(AuthService authService, FitbitApi api, ActivityRepository activityRepository, PatientDeviceRepository patientDeviceRepository, ModelMapper modelMapper, MessageSource messageSource, DeviceRepository deviceRepository, StepsRepository stepsRepository, MinutesRepository minutesRepository) {
        this.authService = authService;
        this.api = api;
        this.activityRepository = activityRepository;
        this.patientDeviceRepository = patientDeviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.deviceRepository = deviceRepository;
        this.stepsRepository = stepsRepository;
        this.minutesRepository = minutesRepository;
    }

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public void getDataOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException, ParseException, FitbitAPIException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("steps", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("calories", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        //ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("distance", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        if (activitiesSteps.getValue() == 0 || activitiesCalories.getValue() == 0)
            throw new FitbitAPIException();
        else {
            saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
            saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
        }
        //this.saveDistanceOfDayFromApiInDB(activitiesDistance, deviceDto);
    }

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public void getDataOfDayBetweenTwoTimesPerMinuteFromApi(String date, String endDate, String startTime, String endTime, Timestamp t1, Timestamp t2, Timestamp syncTime, DeviceDto deviceDto) throws IOException, ParseException, FitbitAPIException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("steps", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        System.out.println("+++++++++  " + activitiesSteps.toString());
        activitiesSteps.setTimeStart(t1);
        activitiesSteps.setTimeEnd(t2);
        activitiesSteps.setSyncTime(syncTime);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("calories", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        activitiesCalories.setTimeStart(t1);
        activitiesCalories.setTimeEnd(t2);
        activitiesCalories.setSyncTime(syncTime);
        if (activitiesSteps.getValue() == 0 || activitiesCalories.getValue() == 0){
            System.out.println("+++++++++  FitbitAPIException");
            throw new FitbitAPIException();
        }
        else {
            saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
            saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
        }
        /*ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("distance", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        activitiesDistance.setTimeStart(t1);
        activitiesDistance.setTimeEnd(t2);
        activitiesDistance.setSyncTime(syncTime);
        saveDistanceOfDayFromApiInDB(activitiesDistance, deviceDto);*/
    }

    //------------------------------------------------------------------- 1day 1min
    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override//----------------------------- steps
    public Response getStepsOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) {
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
        //System.out.println("*------------------  " + deviceDto.getId());
        PatientDevice patientDevice = patientDeviceRepository.getByDeviceIdAndReturnedAtIsNull(deviceDto.getId());
        //System.out.println("*------------------  " + patientDevice.toString());
        activitiesSteps.setPatientDevice(patientDevice);
        patientDevice.getActivitiesSteps().add(activitiesSteps);
        patientDeviceRepository.save(patientDevice);
    }

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override//----------------------------- calories
    public Response getCaloriesOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) {
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
        PatientDevice patientDevice = patientDeviceRepository.getByDeviceIdAndReturnedAtIsNull(deviceDto.getId());
        activitiesCalories.setPatientDevice(patientDevice);
        patientDevice.getActivitiesCalories().add(activitiesCalories);
        patientDeviceRepository.save(patientDevice);
    }

    @Override
    public void saveDistanceOfDayFromApiInDB(ActivitiesDistance activitiesDistance, DeviceDto deviceDto) {
        PatientDevice patientDevice = patientDeviceRepository.getByDeviceIdAndReturnedAtIsNull(deviceDto.getId());
        activitiesDistance.setPatientDevice(patientDevice);
        patientDevice.getActivitiesDistance().add(activitiesDistance);
        patientDeviceRepository.save(patientDevice);
    }

    //*------------------------------------------------------------------------ 1day 2times 1min
    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public Response getStepsOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) {
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

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromApi(String date, String startTime, String endTime, DeviceDto deviceDto) {
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
    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public Response getActivitiesBetween2DatesFromApi(String date1, String date2, DeviceDto deviceDto) {
        try {
            List<Activity> activities = api.getActivities().getActivities(date1, date2, authService.getAccessToken(deviceDto.dtoToObj(modelMapper)));
            Type activitiesDto = new TypeToken<List<ActivityDto>>() {
            }.getType();
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
    public Response getStepsOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) {
        return null;
    }

    @Override
    public Response getCaloriesOfDayPerMinuteFromDB(String date, DeviceDto deviceDto) {
        return null;
    }

    @Override
    public Response getStepsOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) {
        return null;
    }

    @Override
    public Response getCaloriesOfDayBetweenTwoTimesPerMinuteFromDB(String date, String startTime, String endTime, DeviceDto deviceDto) {
        return null;
    }

    @Override
    public Response getActivitiesBetween2DatesFromDB(String date1, String date2, DeviceDto deviceDto) {
        return null;
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    @Override
    public Response getStepsPerVisits(String medicalFileId, List<Date> dates) {
        List<StepsDto> stepsDtoList = new ArrayList<>();
        Map<String, List<StepsDto>> stepsDtoMap = new HashMap<>();
        Type stepsDtoType = new TypeToken<List<StepsDto>>() {}.getType();
        List<PatientDevice> patientDevice = patientDeviceRepository.getByMedicalFileId(medicalFileId);
        if (patientDevice == null )
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        Timestamp initDate = patientDevice.get(0).getInitDate();
        System.out.println("*********************initDate= "+initDate);
        dates = dates.stream().distinct().collect(Collectors.toList());
        System.out.println("*********************dates= "+dates.toString());
        long days = ChronoUnit.DAYS.between(initDate.toLocalDateTime().toLocalDate(), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toLocalDateTime().toLocalDate());
        System.out.println("---- days = " + days);
        System.out.println(Date.valueOf(initDate.toLocalDateTime().toLocalDate()));
        System.out.println(new ArrayList<Steps>().add(stepsRepository.getByMedicalFileIdAndOneDate(
                medicalFileId,
                Date.valueOf(initDate.toLocalDateTime().toLocalDate())
        )));
        if (dates.isEmpty() || days == 0) {
            List<Steps> stepsList = new ArrayList<>();
            stepsList.add(stepsRepository.getByMedicalFileIdAndOneDate(
                    medicalFileId,
                    Date.valueOf(initDate.toLocalDateTime().toLocalDate())
            ));
            stepsDtoList = modelMapper.map(stepsList, stepsDtoType);
            stepsDtoMap.put(Date.valueOf(initDate.toLocalDateTime().toLocalDate()).toString(), stepsDtoList);
            return new Response(stepsDtoMap, null);
        } else {
            //TODO Delete - TimeUnit.MINUTES.toMillis(240)

            for (int i = 0; i < dates.size(); i++) {
                if ( i == 0){
                    stepsDtoList = modelMapper.map(
                            stepsRepository.getByMedicalFileIdAndTwoDates(
                                    medicalFileId,
                                    new Date(initDate.getTime()),
                                    dates.get(i)
                            ),
                            stepsDtoType); //TODO: delete
                    stepsDtoMap.put(dates.get(i).toString(), stepsDtoList);
                    System.out.println("...... "+stepsDtoMap.toString());
                } else {
                    System.out.println("//////// i = "+ i +"   ######### d1 = "+ dates.get(i-1) + "    #########  d2 = "+ dates.get(i));
                    stepsDtoList = modelMapper.map(
                            stepsRepository.getByMedicalFileIdAndTwoDates(
                                    medicalFileId,
                                    dates.get(i-1),
                                    dates.get(i)
                            ),
                            stepsDtoType);
                    stepsDtoMap.put(dates.get(i).toString(), stepsDtoList);
                }

            }
            System.out.println("+++++  "+dates.size());
            System.out.println("+++++ d1 = "+dates.get(dates.size()-1));
            System.out.println("+++++ d2 = "+dates.size());

            System.out.println("******   "+stepsDtoMap.toString());
            return new Response(stepsDtoMap, null);
        }
    }

    //0 - sedentary; 1 - lightly active; 2 - fairly active; 3 - very active.
    @Transactional(readOnly = true)
    @Override
    public Response getActiveMinutesPerVisits(String medicalFileId, List<Date> dates) {
        List<MinutesDto> minutesDtoList = new ArrayList<>();
        Map<String, List<MinutesDto>> minutesDtoMap = new HashMap<>();
        Type minutesDtoType = new TypeToken<List<MinutesDto>>() {}.getType();
        List<PatientDevice> patientDevice = patientDeviceRepository.getByMedicalFileId(medicalFileId);
        if (patientDevice == null )
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        Timestamp initDate = patientDevice.get(0).getInitDate();
        dates = dates.stream().distinct().collect(Collectors.toList());
        long days = ChronoUnit.DAYS.between(initDate.toLocalDateTime().toLocalDate(), new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()).toLocalDateTime().toLocalDate());
        System.out.println("---- days = " + days);
        if (dates.isEmpty() || days == 0) {
            List<Minutes> minutesList = new ArrayList<>();
            minutesList.add(minutesRepository.getByMedicalFileIdAndOneDate(
                    medicalFileId,
                    Date.valueOf(initDate.toLocalDateTime().toLocalDate())
            ));
            minutesDtoList = modelMapper.map(minutesList, minutesDtoType);
            minutesDtoMap.put(Date.valueOf(initDate.toLocalDateTime().toLocalDate()).toString(), minutesDtoList);

            return new Response(minutesDtoMap, null);
        } else {
            //TODO Delete - TimeUnit.MINUTES.toMillis(240)

            for (int i = 0; i < dates.size(); i++) {
                if (i == 0){
                    minutesDtoList = modelMapper.map(
                            minutesRepository.getByMedicalFileIdAndTwoDates(
                                    medicalFileId,
                                    new Date(initDate.getTime()),
                                    dates.get(i)
                            ),
                            minutesDtoType);
                    minutesDtoMap.put(dates.get(i).toString(), minutesDtoList);
                } else{
                    minutesDtoList = modelMapper.map(
                            minutesRepository.getByMedicalFileIdAndTwoDates(
                                    medicalFileId,
                                    dates.get(i),
                                    dates.get(i - 1)
                            ),
                            minutesDtoType);
                    minutesDtoMap.put(dates.get(i).toString(), minutesDtoList);
                }

            }
            return new Response(minutesDtoMap, null);
        }
    }

}
