package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.event.reminder.OnSynchronizationEmailEvent;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.activity.ActivityService;
import ca.uqtr.fitbit.service.auth.AuthService;
import javassist.bytecode.stackmap.TypeData;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
//@Transactional
public class DeviceServiceImpl implements DeviceService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final static String COLLECTION_PATH = "activities";

    private DeviceRepository deviceRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private AuthService authService;
    private FitbitApi fitbitApi;
    private ActivityService activityService;

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
            return new Response(device, null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

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

    @Override
    public Response readAvailableDevicesByInstitutionCode(DeviceDto device, String patientId) {
        try{
            Device device1 = deviceRepository.isPatientHasDevice(UUID.fromString(patientId));
            if (device1 != null){
                return new Response(modelMapper.map(device1, DeviceDto.class), null);
            }
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            List<Device> devices = deviceRepository.findAllByInstitutionCodeAndAvailableAndAuthorized(device.getInstitutionCode(), true, true);
            return new Response(modelMapper.map(devices, deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response assignDevice(DeviceDto device) {
        try{
            Device device1 = deviceRepository.getDeviceById(device.getId());
            if (device1 == null)
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            List<PatientDevice> patientDevices = device1.getPatientDevices();
            PatientDevice patientDevice = modelMapper.map(device.getPatientDevices().get(0), PatientDevice.class);
            patientDevice.setInitDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            patientDevice.setDevice(device1);
            patientDevices.add(patientDevice);
            device1.setAvailable(false);
            device1.setLastSyncDate(new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()));
            //device1.get().setLastSyncDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            return new Response(modelMapper.map(deviceRepository.save(device1), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response getBackDevice(DeviceDto device) {
        try{
            Device device2 = deviceRepository.getDeviceWith_LastPatientDevice_FetchTypeEAGER(device.getId());
            if (device2 == null)
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            device2.setAvailable(true);
            device2.getPatientDevices().get(0).setReturnedAt(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            return new Response(modelMapper.map(deviceRepository.save(device2), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

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

    @Override
    public Response allSubscriptions(DeviceDto device) throws IOException {

        return fitbitApi.allSubscriptions(authService.getAccessToken(device.dtoToObj(modelMapper)), COLLECTION_PATH);
    }

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
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            long d1 = device1.get().getLastSyncDate().getTime();
            //TODO Delete - TimeUnit.MINUTES.toMillis(240)
            long minutes = TimeUnit.MILLISECONDS.toMinutes(cal.getTime().getTime() - d1 - TimeUnit.MINUTES.toMillis(240));
            int j = (int) (minutes/1440);
            long d2 = d1 + TimeUnit.MINUTES.toMillis(1439);
            for (int i = 0; i < j; i++) {
                System.out.println("d1 =   "+new Date(d1).toLocalDate() +"   d2   "+ new Date(d2).toLocalDate());
                System.out.println("d1 =   "+ new Time(d1).toString().substring(0,5)+"   d2   "+ new Time(d2).toString().substring(0,5));
                activityService.getDataOfDayBetweenTwoTimesPerMinuteFromApi(
                        new Date(d1).toLocalDate().toString(),
                        new Date(d2).toLocalDate().toString(),
                        new Time(d1).toString().substring(0,5),
                        new Time(d2).toString().substring(0,5),
                        new Timestamp(d1), new Timestamp(d2),
                        device);
                minutes -= 1440;
                if (minutes >= 1440){
                    d1 = d2 + TimeUnit.MINUTES.toMillis(1);
                    d2 = d1 + TimeUnit.MINUTES.toMillis(1439);
                }
            }
            if (minutes > 0){
                d1 = d2 + TimeUnit.MINUTES.toMillis(1);
                d2 = d1 + TimeUnit.MINUTES.toMillis(minutes);
                System.out.println("d1 =   "+new Date(d1 ).toLocalDate() +"   d2   "+ new Date(d2 ).toLocalDate());
                System.out.println("d1 =   "+ new Time(d1).toString().substring(0,5)+"   d2   "+ new Time(d2).toString().substring(0,5));
                activityService.getDataOfDayBetweenTwoTimesPerMinuteFromApi(
                        new Date(d1 ).toLocalDate().toString(),
                        new Date(d2 ).toLocalDate().toString(),
                        new Time(d1).toString().substring(0,5),
                        new Time(d2).toString().substring(0,5),
                        new Timestamp(d1), new Timestamp(d2),
                        device);
            }
            device1.get().setLastSyncDate(new Timestamp(d2 + TimeUnit.MINUTES.toMillis(1)));
            deviceRepository.save(device1.get());
            return new Response(device, null);
        } catch (Exception ex){
            LOGGER.log( Level.ALL, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }

    }

}
