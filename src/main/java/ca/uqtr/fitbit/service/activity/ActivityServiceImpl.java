package ca.uqtr.fitbit.service.activity;


import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.ActivityDto;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Response;
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
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


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
    @PersistenceContext
    private final EntityManager entityManager;


    @Autowired
    public ActivityServiceImpl(AuthService authService, FitbitApi api, ActivityRepository activityRepository, PatientDeviceRepository patientDeviceRepository, ModelMapper modelMapper, MessageSource messageSource, DeviceRepository deviceRepository, EntityManager entityManager) {
        this.authService = authService;
        this.api = api;
        this.activityRepository = activityRepository;
        this.patientDeviceRepository = patientDeviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.deviceRepository = deviceRepository;
        this.entityManager = entityManager;
    }

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public void getDataOfDayPerMinuteFromApi(String date, DeviceDto deviceDto) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("steps", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("calories", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayPerMinute("distance", date, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        this.saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
        this.saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
        this.saveDistanceOfDayFromApiInDB(activitiesDistance, deviceDto);
    }

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public void getDataOfDayBetweenTwoTimesPerMinuteFromApi(String date, String endDate, String startTime, String endTime, Timestamp t1, Timestamp t2, Timestamp syncTime, DeviceDto deviceDto) throws IOException, ParseException {
        ActivitiesSteps activitiesSteps = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("steps", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesSteps.class);
        activitiesSteps.setTimeStart(t1);
        activitiesSteps.setTimeEnd(t2);
        activitiesSteps.setSyncTime(syncTime);
        ActivitiesCalories activitiesCalories = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("calories", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesCalories.class);
        activitiesCalories.setTimeStart(t1);
        activitiesCalories.setTimeEnd(t2);
        activitiesCalories.setSyncTime(syncTime);
        ActivitiesDistance activitiesDistance = modelMapper.map(api.getActivitiesTypeData().getDataOfDayBetweenTwoTimePerMinute("distance", date, endDate, startTime, endTime, authService.getAccessToken(deviceDto.dtoToObj(modelMapper))), ActivitiesDistance.class);
        activitiesDistance.setTimeStart(t1);
        activitiesDistance.setTimeEnd(t2);
        activitiesDistance.setSyncTime(syncTime);
        saveStepsOfDayFromApiInDB(activitiesSteps, deviceDto);
        saveCaloriesOfDayFromApiInDB(activitiesCalories, deviceDto);
        saveDistanceOfDayFromApiInDB(activitiesDistance, deviceDto);
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
        System.out.println("*------------------  " + deviceDto.getId());
        PatientDevice patientDevice = patientDeviceRepository.getByDeviceIdAndReturnedAtIsNull(deviceDto.getId());
        System.out.println("*------------------  " + patientDevice.toString());
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

    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
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
    @Retryable(
            value = {Exception.class},
            backoff = @Backoff(delay = 3000))
    @Override
    public Response getActivitiesBetween2DatesFromApi(String date1, String date2, DeviceDto deviceDto) throws IOException, ParseException {
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

    @Transactional(readOnly = true)
    @Override
    public Integer getStepsPerVisitsDAO(String medicalFileId, String date1, String date2) {
        return entityManager.createQuery("select sum(steps.value) from PatientDevice pd left join fetch pd.activitiesSteps steps where pd.id = steps.patientDevice.id and pd.medicalFileId = :medicalFileId and steps.dateTime between :date1 and :date2 group by steps.dateTime")
                .setParameter("medicalFileId", medicalFileId)
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .getFirstResult();

    }

    @Transactional(readOnly = true)
    @Override
    public Response getStepsPerVisits(String medicalFileId, List<Timestamp> dates) {
        /*Timestamp creationDate = dates.remove(0);
        List<Timestamp> visits = dates;
        Map<String, Integer> steps = new HashMap<>();
        int i = 0;
        if (dates.isEmpty())
            steps.put(, patientDeviceRepository.getStepsBetweenTwoVisits(medicalFileId, creationDate, new java.sql.Timestamp(Calendar.getInstance().getTime().getTime())));
        visits.forEach(item ->{
            if (i == steps.size())
                return;
            patientDeviceRepository.getStepsBetweenTwoVisits(medicalFileId, )

        });*/

        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Response getActiveMinutesPerVisits(String medicalFileId, List<Timestamp> dates) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Response getActiveMinutesPerVisitsDAO(String medicalFileId, List<Timestamp> dates) {
        return null;
    }

}
