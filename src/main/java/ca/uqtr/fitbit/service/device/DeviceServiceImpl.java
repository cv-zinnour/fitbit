package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.activity.ActivityService;
import ca.uqtr.fitbit.service.auth.AuthService;
import javassist.bytecode.stackmap.TypeData;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final static String COLLECTION_PATH = "activities";

    private MessageSource messageSource;
    private DeviceRepository deviceRepository;
    private ModelMapper modelMapper;
    private AuthService authService;
    private FitbitApi fitbitApi;
    private ActivityService activityService;
    @Value("${sha3-256.salt}")
    private String SALT;
    public static final String SHA3_256 = "SHA3-256";

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, ModelMapper modelMapper, MessageSource messageSource, AuthService authService, FitbitApi fitbitApi, ActivityService activityService) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.authService = authService;
        this.fitbitApi = fitbitApi;
        this.activityService = activityService;
    }

    @Override
    public Response createDevice(DeviceDto device) {
        try{
            device.setAvailable(true);
            return new Response(modelMapper.map(deviceRepository.save(device.dtoToObj(modelMapper)), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response readDevice(DeviceDto device) {
        try{
            return new Response(modelMapper.map(deviceRepository.findById(device.getId()), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public void deleteDevice(DeviceDto device) {
        try{
            deviceRepository.deleteById(device.dtoToObj(modelMapper).getId());
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
        }
    }

    @Override
    public Response updateDevice(DeviceDto device) {
        try{
            return new Response(modelMapper.map(deviceRepository.save(device.dtoToObj(modelMapper)), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
    @Transactional( readOnly = true )
    @Override
    public Response readDevices(DeviceDto device) {
        try{
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            return new Response(modelMapper.map(deviceRepository.findAllByAdminId(device.getAdminId()), deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response authorizeDevice(DeviceDto device, String code) {
        try{
            Optional<Device> device1 = deviceRepository.findById(device.getId());
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            Auth auth = authService.authorizationCode2AccessAndRefreshToken(code, device1.get());
            if (auth == null)
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            device1.get().setAuthorized(true);
            addSubscription(device);
            return new Response(modelMapper.map(device1, DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response unauthorizeDevice(DeviceDto device) {
        try{
            Optional<Device> device1 = deviceRepository.findById(device.getId());
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            authService.deleteById(device1.get().getId());
            removeSubscription(device);
            return new Response(device, null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }
    @Transactional( readOnly = true )
    @Override
    public Response readAvailableDevices(DeviceDto device) {
        try{
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            return new Response(modelMapper.map(deviceRepository.findAllByAdminIdAndAvailable(device.getAdminId(), true), deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Transactional( readOnly = true )
    @Override
    public Response readAvailableDevicesByInstitutionCode(DeviceDto device, String patientId) {
        String patientIdSHA = new DigestUtils(SHA3_256).digestAsHex(patientId.concat(SALT));

        Device device1 = deviceRepository.isPatientHasDevice(patientIdSHA);
            if (device1 != null){
                return new Response(modelMapper.map(device1, DeviceDto.class), null);
            }
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            List<Device> devices = deviceRepository.findAllByInstitutionCodeAndAvailableAndAuthorized(device.getInstitutionCode(), true, true);
            return new Response(modelMapper.map(devices, deviceDtoList), null);

    }

    @SneakyThrows
    @Transactional
    @Override
    public Response assignDevice(DeviceDto device) {
        Device device1 = deviceRepository.getDeviceById(device.getId());
        if (device1 == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        List<PatientDevice> patientDevices = device1.getPatientDevices();
        System.out.println("************  "+ patientDevices.size());
        PatientDevice patientDevice = modelMapper.map(device.getPatientDevices().get(0), PatientDevice.class);
        //TODO Delete - TimeUnit.MINUTES.toMillis(240)
        patientDevice.setInitDate(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        patientDevice.setDevice(device1);
        patientDevices.add(patientDevice);
        device1.setAvailable(false);
        //TODO Delete - TimeUnit.MINUTES.toMillis(240)
        device1.setLastSyncDate(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
        return new Response(modelMapper.map(deviceRepository.save(device1), DeviceDto.class), null);
    }

    @Retryable(
            value = { Exception.class },
            backoff = @Backoff(delay = 3000))
    @Override
    public Response updateFitbitProfile(DeviceDto device, String gender, String birthday, String height) throws IOException {
        return fitbitApi.updateProfile(
                authService.getAccessToken(device.dtoToObj(modelMapper)),
                gender,
                birthday,
                height);
    }

    @Retryable(
            value = { Exception.class },
            backoff = @Backoff(delay = 3000))
    @Override
    public Response updateFitbitWeight(DeviceDto device, String weight) throws IOException {
        Calendar cal = Calendar.getInstance();
        //TODO Delete - TimeUnit.MINUTES.toMillis(240)
        long date = cal.getTime().getTime();
        return fitbitApi.updateWeight(
                authService.getAccessToken(device.dtoToObj(modelMapper)),
                weight,
                new Date(date).toLocalDate().toString(),
                new Time(date).toString().substring(0,5));
    }

    @Transactional
    @Override
    public Response getBackDevice(DeviceDto device) {
        try{
            Device device2 = deviceRepository.getDeviceWith_LastPatientDevice_FetchTypeEAGER(device.getId());
            if (device2 == null)
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            device2.setAvailable(true);
            device2.getPatientDevices().get(0).setReturnedAt(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            return new Response(modelMapper.map(deviceRepository.save(device2), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Retryable(
            value = { Exception.class },
            backoff = @Backoff(delay = 3000))
    @Override
    public Response addSubscription(DeviceDto device) throws IOException {

        Response response = fitbitApi.addSubscription(new FitbitSubscription(device.getId().toString()), authService.getAccessToken(device.dtoToObj(modelMapper)), COLLECTION_PATH);
        if (response.getObject() == null)
            return response;
        Optional<Device> device1 = deviceRepository.findById(device.getId());
        if (!device1.isPresent())
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        device1.get().getFitbitSubscriptions().add((FitbitSubscription) response.getObject());
        return new Response(modelMapper.map(deviceRepository.save(device1.get()), DeviceDto.class), null);
    }

    @Retryable(
            value = { Exception.class },
            backoff = @Backoff(delay = 3000))
    @Override
    public Response allSubscriptions(DeviceDto device) throws IOException {

        return fitbitApi.allSubscriptions(authService.getAccessToken(device.dtoToObj(modelMapper)), COLLECTION_PATH);
    }

    @Retryable(
            value = { Exception.class },
            backoff = @Backoff(delay = 3000))
    @Transactional( readOnly = true )
    @Override
    public Response removeSubscription(DeviceDto device) throws IOException {
        Device device1 = deviceRepository.getDeviceWith_LastFitbitSubscription_FetchTypeEAGER(device.getId());
        if (device1 == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        return fitbitApi.removeSubscription(device1.getFitbitSubscriptions().get(0), authService.getAccessToken( device.dtoToObj(modelMapper)), COLLECTION_PATH);
    }

    @Override
    public Flux<DeviceDto> getDevicesNotReturned() {
        /*Flux<Device> devices = deviceReactiveRepository.devicesNotReturned();
        return devices.map(device -> modelMapper.map(device, DeviceDto.class));*/
        return null;
    }

    @Override
    public Response getDataFromAPIToDB(DeviceDto device) {
        Calendar cal = Calendar.getInstance();
        try{
            Optional<Device> device1 = deviceRepository.findById(device.getId());
            if (device1.isEmpty()){
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            }
            Timestamp syncTime = new Timestamp(cal.getTime().getTime());
            long lastSyncDate = device1.get().getLastSyncDate().getTime();
            List<TwoDates> datesList = this.datesListBetweenTwoDate(new Timestamp(lastSyncDate), syncTime);
            LOGGER.info("datesList: "+ datesList.toString());

            for (int i = 0; i < datesList.size(); i++) {
                Timestamp d1 = datesList.get(i).getDate1();
                Timestamp d2 = datesList.get(i).getDate2();

                activityService.getDataOfDayBetweenTwoTimesPerMinuteFromApi(
                        d1.toLocalDateTime().toLocalDate().toString(),
                        d2.toLocalDateTime().toLocalDate().toString(),
                        Time.valueOf(d1.toLocalDateTime().toLocalTime()).toString().substring(0,5),
                        Time.valueOf(d2.toLocalDateTime().toLocalTime()).toString().substring(0,5),
                        d1, d2,
                        syncTime,
                        device);
                device1.get().setLastSyncDate(Timestamp.valueOf(d2.toLocalDateTime().toLocalDate().atStartOfDay().plus(Duration.of(1, ChronoUnit.MINUTES))));
                deviceRepository.save(device1.get());
            }
            return new Response(device, null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }

    }

    @Override
    public Response isFitbitProfileAssigned(DeviceDto deviceDto) throws IOException {
        String encodedId = authService.getFitbitProfileId(modelMapper.map(deviceDto, Device.class));
        if (encodedId == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        return new Response(encodedId, null);
    }


    public List<TwoDates> datesListBetweenTwoDate(Timestamp s, Timestamp e){
        LocalDate start = s.toLocalDateTime().toLocalDate();
        LocalDate end = e.toLocalDateTime().toLocalDate();
        List<LocalDate> totalDates = new ArrayList<>();
        while (!start.isAfter(end)) {
            totalDates.add(start);
            start = start.plusDays(1);
        }
        totalDates.remove(0);
        List<TwoDates> dd = new ArrayList<>();

        for (int i = 0; i < totalDates.size(); i++) {
            TwoDates td = new TwoDates();

            if (i == 0){
                td.setDate1(s);
            } else {
                td.setDate1(Timestamp.valueOf(totalDates.get(i-1).atStartOfDay().plus(Duration.of(0, ChronoUnit.MINUTES))));
            }
            td.setDate2(Timestamp.valueOf(totalDates.get(i).atStartOfDay().minus(Duration.of(1, ChronoUnit.MINUTES))));
            dd.add(td);
        }
        dd.add(new TwoDates(Timestamp.valueOf(totalDates.get(totalDates.size() - 1).atStartOfDay().plus(Duration.of(0, ChronoUnit.MINUTES))), e));
        return dd;

    }
    public static class TwoDates{
        private Timestamp date1;
        private Timestamp date2;

        public TwoDates() {
        }

        public TwoDates(Timestamp date1, Timestamp date2) {
            this.date1 = date1;
            this.date2 = date2;
        }

        public void setDate1(Timestamp date1) {
            this.date1 = date1;
        }

        public void setDate2(Timestamp date2) {
            this.date2 = date2;
        }

        public Timestamp getDate1() {
            return date1;
        }

        public Timestamp getDate2() {
            return date2;
        }

        @Override
        public String toString() {
            return "TwoDates{" +
                    "date1=" + date1 +
                    ", date2=" + date2 +
                    '}';
        }
    }

}
