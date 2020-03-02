package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.PatientDeviceDto;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.auth.AuthService;
import javassist.bytecode.stackmap.TypeData;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );
    private final static String COLLECTION_PATH = "activities";

    private DeviceRepository deviceRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private AuthService authService;
    private FitbitApi fitbitApi;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, ModelMapper modelMapper, MessageSource messageSource, AuthService authService, FitbitApi fitbitApi) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.authService = authService;
        this.fitbitApi = fitbitApi;
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
        System.out.println(device.toString());
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
        System.out.println(device.toString());
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
    public Response readAvailableDevicesByInstitutionCode(DeviceDto device) {
        try{
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
            Optional<Device> device1 = deviceRepository.findById(device.getId());
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            List<PatientDevice> patientDevices = device1.get().getPatientDevices();
            patientDevices.add(modelMapper.map(device.getPatientDevices().get(0), PatientDevice.class));
            device1.get().setAvailable(false);
            device1.get().setLastSyncDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            return new Response(modelMapper.map(deviceRepository.save(device1.get()), DeviceDto.class), null);
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
            Optional<Device> device1 = deviceRepository.findById(device.getId());
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            device1.get().setAvailable(true);
            return new Response(modelMapper.map(deviceRepository.save(device1.get()), DeviceDto.class), null);
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
        System.out.println(device1.getFitbitSubscriptions().get(0).toString());
        return fitbitApi.removeSubscription(device1.getFitbitSubscriptions().get(0), authService.getAccessToken( device.dtoToObj(modelMapper)), COLLECTION_PATH);
    }

}
