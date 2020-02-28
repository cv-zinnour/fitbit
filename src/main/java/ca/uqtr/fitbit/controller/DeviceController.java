package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Error;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.Auth;
import ca.uqtr.fitbit.service.auth.AuthService;
import ca.uqtr.fitbit.service.device.DeviceService;
import ca.uqtr.fitbit.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping
public class DeviceController {

    private final DeviceService deviceService;
    @Value("${fitbit.subscription.verification-code}")
    private String fitbitVerificationCode;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping(value = "/device")
    @ResponseBody
    public Response createDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        return deviceService.createDevice(deviceDto);
    }

    @GetMapping(value = "/device")
    @ResponseBody
    public Response readDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevice(deviceDto);
    }

    @DeleteMapping(value = "/device")
    @ResponseBody
    public void deleteDevice(@RequestBody Request request){
        deviceService.deleteDevice((DeviceDto) request.getObject());
    }

    @PutMapping(value = "/device")
    @ResponseBody
    public Response updateDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.updateDevice(deviceDto);
    }

    @GetMapping(value = "/device/all")
    @ResponseBody
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
    @GetMapping(value = "/device/all/available")
    @ResponseBody
    public Response readAvailableDevices(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevices(deviceDto);
    }

    @PostMapping(value = "/device/all/available/institution")
    @ResponseBody
    public Response readAvailableDevicesByInstitutionCode(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevicesByInstitutionCode(deviceDto);
    }

    @PostMapping(value = "/device/assign")
    @ResponseBody
    public Response assignDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.assignDevice(deviceDto);
    }

    @PostMapping(value = "/device/back")
    @ResponseBody
    public Response getBackDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = (DeviceDto) request.getObject();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.getBackDevice(deviceDto);
    }


    @GetMapping("/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotification(@RequestParam String verify) {
        if(verify.equals(fitbitVerificationCode)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/subscription/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotificationData(@PathParam("version") String version, List notificationList) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        //Runnable taskOne = new FitbitDataThread(notificationList);
        //executor.execute(taskOne);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
