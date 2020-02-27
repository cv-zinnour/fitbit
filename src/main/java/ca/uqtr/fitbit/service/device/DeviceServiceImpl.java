package ca.uqtr.fitbit.service.device;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.Device;
import ca.uqtr.fitbit.entity.PatientDevice;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javassist.bytecode.stackmap.TypeData;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DeviceServiceImpl implements DeviceService {
    private static final Logger LOGGER = Logger.getLogger( TypeData.ClassName.class.getName() );

    private DeviceRepository deviceRepository;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private AuthService authService;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, ModelMapper modelMapper, MessageSource messageSource, AuthService authService) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.authService = authService;
    }

    @Override
    public Response createDevice(DeviceDto device) {
        try{
            return new Response(modelMapper.map(deviceRepository.save(device.dtoToObj(modelMapper)), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response readDevice(DeviceDto device) {
        try{
            return new Response(modelMapper.map(deviceRepository.findById(UUID.fromString(device.getId())), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public void deleteDevice(DeviceDto device) {
        try{
            deviceRepository.delete(device.dtoToObj(modelMapper));
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
        }
    }

    @Override
    public Response updateDevice(DeviceDto device) {
        try{
            return new Response(modelMapper.map(deviceRepository.save(device.dtoToObj(modelMapper)), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response readDevices(DeviceDto device) {
        try{
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            return new Response(modelMapper.map(deviceRepository.findAllByAdminId(UUID.fromString(device.getId())), deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response authorizeDevice(DeviceDto device, String code) {
        try{
            Optional<Device> device1 = deviceRepository.findById(UUID.fromString(device.getId()));
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
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response unauthorizeDevice(DeviceDto device) {
        try{
            Optional<Device> device1 = deviceRepository.findById(UUID.fromString(device.getId()));
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            authService.deleteById(device1.get().getId());
            return new Response(device, null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response readAvailableDevices(DeviceDto device) {
        try{
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            return new Response(modelMapper.map(deviceRepository.findAllByAdminIdAndAvailable(UUID.fromString(device.getId()), true), deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response readAvailableDevicesByInstitutionCode(DeviceDto device) {
        try{
            Type deviceDtoList = new TypeToken<List<DeviceDto>>() {}.getType();
            return new Response(modelMapper.map(deviceRepository.findAllByInstitutionCodeAndAvailable(UUID.fromString(device.getInstitutionCode()), true), deviceDtoList), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        }
    }

    @Override
    public Response assignDevice(DeviceDto device) {
        try{
            Optional<Device> device1 = deviceRepository.findById(UUID.fromString(device.getId()));
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            List<PatientDevice> patientDevices = device1.get().getPatientDevices();
            patientDevices.add(device.getPatientDevices().get(0));
            device1.get().setAvailable(false);
            device1.get().setLastSyncDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
            return new Response(modelMapper.map(deviceRepository.save(device1.get()), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));

        }
    }

    @Override
    public Response getBackDevice(DeviceDto device) {
        try{
            Optional<Device> device1 = deviceRepository.findById(UUID.fromString(device.getId()));
            if (!device1.isPresent())
                return new Response(null,
                        new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                                messageSource.getMessage("error.null.message", null, Locale.US)));
            device1.get().setAvailable(true);
            return new Response(modelMapper.map(deviceRepository.save(device1.get()), DeviceDto.class), null);
        } catch (Exception ex){
            LOGGER.log( Level.WARNING, ex.getMessage());
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));

        }
    }
}
