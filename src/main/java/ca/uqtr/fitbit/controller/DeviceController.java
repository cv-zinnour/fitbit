package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.service.auth.AuthService;
import ca.uqtr.fitbit.service.device.DeviceService;
import ca.uqtr.fitbit.utils.JwtTokenUtil;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public Response createDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        return deviceService.createDevice(deviceDto);
    }

    public Response readDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevice(deviceDto);
    }

    public void deleteDevice(@RequestBody Request request){
        deviceService.deleteDevice((DeviceDto) request.getObject());
    }

    public Response updateDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.updateDevice(deviceDto);
    }

    public Response readDevices(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevices(deviceDto);
    }

    @CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @GetMapping(value = "/device/authorization")
    public Response authorizeDevice(@RequestParam String code, @RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.authorizeDevice(deviceDto, code);
    }

    public Response readAvailableDevices(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevices(deviceDto);
    }

    public Response readAvailableDevicesByInstitutionCode(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevicesByInstitutionCode(deviceDto);
    }

    public Response assignDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.assignDevice(deviceDto);
    }

    public Response getBackDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.getBackDevice(deviceDto);
    }
}
