package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.service.device.DeviceService;
import ca.uqtr.fitbit.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping
public class DeviceController {

    private final DeviceService deviceService;
    @Value("${fitbit.subscription.verification-code}")
    private String fitbitVerificationCode;
    private ObjectMapper mapper;

    public DeviceController(DeviceService deviceService, ObjectMapper mapper) {
        this.deviceService = deviceService;
        this.mapper = mapper;
    }

    @PostMapping(value = "/device")
    @ResponseBody
    public Response createDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        System.out.println(deviceDto.toString());
        return deviceService.createDevice(deviceDto);
    }

    @GetMapping(value = "/device")
    @ResponseBody
    public Response readDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        return deviceService.readDevice(deviceDto);
    }

    @DeleteMapping(value = "/device")
    @ResponseBody
    public boolean deleteDevice(@RequestBody Request request) throws IOException {
        System.out.println(request.toString());
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        /*Response response1 = deviceService.removeSubscription(deviceDto);
        if(response1 == null)
            return false;*/
        System.out.println("+++++++++++++++++++++++++++++++++++++++ "+deviceService.allSubscriptions(deviceDto).getObject().toString());
        deviceService.deleteDevice(deviceDto);
        return true;
    }

    @PutMapping(value = "/device")
    @ResponseBody
    public Response updateDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.updateDevice(deviceDto);
    }

    @GetMapping(value = "/device/all")
    @ResponseBody
    public Response readDevices(HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevices(deviceDto);
    }

    //@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @GetMapping(value = "/device/authorization")
    @ResponseBody
    public Response authorizeDevice(@RequestParam String code, @RequestParam String deviceId, HttpServletRequest HttpRequest) throws IOException {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = new DeviceDto(deviceId);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        Response response = deviceService.authorizeDevice(deviceDto, code);
        if (response.getObject() == null)
            return response;
        Response response1 = deviceService.addSubscription(deviceDto);
        System.out.println("+++++++++++++++++++++++++++++++++++++++ "+deviceService.allSubscriptions(deviceDto).getObject().toString());
        return response1;
    }

    @GetMapping(value = "/device/unauthorization")
    @ResponseBody
    public Response unauthorizeDevice(@RequestBody Request request, HttpServletRequest HttpRequest) throws IOException {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        System.out.println("*******************************"+deviceService.allSubscriptions(deviceDto).getObject().toString());

        return deviceService.unauthorizeDevice(deviceDto);
    }

    @GetMapping(value = "/device/all/available")
    @ResponseBody
    public Response readAvailableDevices(HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevices(deviceDto);
    }

    @PostMapping(value = "/device/all/available/institution")
    @ResponseBody
    public Response readAvailableDevicesByInstitutionCode(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevicesByInstitutionCode(deviceDto);
    }

    @PostMapping(value = "/device/assign")
    @ResponseBody
    public Response assignDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.assignDevice(deviceDto);
    }

    @PostMapping(value = "/device/back")
    @ResponseBody
    public Response getBackDevice(@RequestBody Request request, HttpServletRequest HttpRequest){
        String token = HttpRequest.getHeader("Authorization").replace("bearer ","");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.getBackDevice(deviceDto);
    }

    @PostMapping("/subscription/all")
    public Response getAllSubscriptions(@RequestBody Request request) throws IOException {
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        return deviceService.allSubscriptions(deviceDto);
    }

    @GetMapping("/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotification(@RequestParam String verify) {
        if(verify.equals(fitbitVerificationCode)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/subscription")
    public ResponseEntity<HttpStatus> getFitBitNotificationData() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
