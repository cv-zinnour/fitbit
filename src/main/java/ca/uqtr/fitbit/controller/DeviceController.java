package ca.uqtr.fitbit.controller;

import ca.uqtr.fitbit.api.FitbitApi;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Request;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.FitbitSubscription;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.auth.AuthService;
import ca.uqtr.fitbit.service.device.DeviceService;
import ca.uqtr.fitbit.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
public class DeviceController {

    private final DeviceService deviceService;
    @Value("${fitbit.subscription.verification-code}")
    private String fitbitVerificationCode;
    private ObjectMapper mapper;
    private WebClient.Builder webClient;
    @Value("${patient-service.get.profile-infos.url}")
    private String PATIENT_SERVICE_GET_PROFILE_INFOS;
    private MessageSource messageSource;

    public DeviceController(DeviceService deviceService, ObjectMapper mapper, WebClient.Builder webClient, MessageSource messageSource, DeviceRepository deviceRepository, FitbitApi fitbitApi, ModelMapper modelMapper, AuthService authService) {
        this.deviceService = deviceService;
        this.mapper = mapper;
        this.webClient = webClient;
        this.messageSource = messageSource;
        this.deviceRepository = deviceRepository;
        this.fitbitApi = fitbitApi;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @PostMapping(value = "/device")
    @ResponseBody
    public Response createDevice(@RequestBody Request request, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        return deviceService.createDevice(deviceDto);
    }

    @GetMapping(value = "/device")
    @ResponseBody
    public Response readDevice(@RequestBody Request request, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevice(deviceDto);
    }

    @DeleteMapping(value = "/device")
    @ResponseBody
    public boolean deleteDevice(@RequestBody Request request) throws IOException {
        System.out.println(request.toString());
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        Response response1 = deviceService.removeSubscription(deviceDto);
        if(response1 == null)
            return false;

        deviceService.deleteDevice(deviceDto);
        return true;
    }

    @PutMapping(value = "/device")
    @ResponseBody
    public Response updateDevice(@RequestBody Request request, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.updateDevice(deviceDto);
    }

    @GetMapping(value = "/device/all")
    @ResponseBody
    public Response readDevices(HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readDevices(deviceDto);
    }

    //@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
    @GetMapping(value = "/device/authorization")
    @ResponseBody
    public Response authorizeDevice(@RequestParam String code, @RequestParam String deviceId, HttpServletRequest HttpRequest) throws IOException {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = new DeviceDto(deviceId);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        Response response = deviceService.authorizeDevice(deviceDto, code);
        return response;
    }

    @GetMapping(value = "/device/unauthorization")
    @ResponseBody
    public Response unauthorizeDevice(@RequestBody Request request, HttpServletRequest HttpRequest) throws IOException {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        Response response = deviceService.unauthorizeDevice(deviceDto);
        System.out.println(response.toString());
        return response;
    }

    @GetMapping(value = "/device/all/available")
    @ResponseBody
    public Response readAvailableDevices(HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.readAvailableDevices(deviceDto);
    }

    @GetMapping(value = "/device/all/available/institution")
    @ResponseBody
    public Response readAvailableDevicesByInstitutionCode(@RequestParam String patientId, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setInstitutionCode(JwtTokenUtil.getInstitutionCode(token));
        return deviceService.readAvailableDevicesByInstitutionCode(deviceDto, patientId);
    }

    @PostMapping(value = "/device/assign")
    @ResponseBody
    public Response assignDevice(@RequestBody Request request) throws IOException {
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        /*ProfileDto profileDto = webClient.build().get().uri(PATIENT_SERVICE_GET_PROFILE_INFOS + "?medicalFileId=" + deviceDto.getPatientDevices().get(0).getMedicalFileId().toString())
                .retrieve()
                .bodyToMono(ProfileDto.class)
                .block();
        if (profileDto == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.null.id", null, Locale.US)),
                            messageSource.getMessage("error.null.message", null, Locale.US)));
        System.out.println("------ 2 profile dto= "+ profileDto.toString());
        if (profileDto.getBirthday() == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.patient.birthday.id", null, Locale.US)),
                            messageSource.getMessage("error.patient.birthday.message", null, Locale.US)));
        if (profileDto.getGender() == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.patient.gender.id", null, Locale.US)),
                            messageSource.getMessage("error.patient.gender.message", null, Locale.US)));
        if (profileDto.getHeight() == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.patient.height.id", null, Locale.US)),
                            messageSource.getMessage("error.patient.height.message", null, Locale.US)));
        if (profileDto.getWeight() == null)
            return new Response(null,
                    new Error(Integer.parseInt(messageSource.getMessage("error.patient.weight.id", null, Locale.US)),
                            messageSource.getMessage("error.patient.weight.message", null, Locale.US)));*/

//            deviceService.updateFitbitWeight(deviceDto, profileDto.getWeight());
//            deviceService.updateFitbitProfile(deviceDto, profileDto.getGender(), profileDto.getBirthday(), profileDto.getHeight());
            return deviceService.assignDevice(deviceDto);


    }

    @PostMapping(value = "/profile/assigned")
    @ResponseBody
    public Response isFitbitProfileAssigned(@RequestBody Request request) throws IOException {
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        return deviceService.isFitbitProfileAssigned(deviceDto);
    }

    @PostMapping(value = "/device/back")
    @ResponseBody
    public Response getBackDevice(@RequestBody Request request, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.getBackDevice(deviceDto);
    }

    @PostMapping(value = "/device/sync")
    @ResponseBody
    public Response syncDevice(@RequestBody Request request, HttpServletRequest HttpRequest) {
        String token = HttpRequest.getHeader("Authorization").replace("bearer ", "");
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        deviceDto.setAdminId(JwtTokenUtil.getId(token));
        return deviceService.syncDevice(deviceDto);
    }

    @PostMapping("/subscription/all")
    public Response getAllSubscriptions(@RequestBody Request request) throws IOException {
        DeviceDto deviceDto = mapper.convertValue(request.getObject(), DeviceDto.class);
        return deviceService.allSubscriptions(deviceDto);
    }

    @GetMapping("/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotification(@RequestParam String verify) {
        System.out.println("////////////////////////  getFitBitNotification");
        if (verify.equals(fitbitVerificationCode)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // @JsonInclude(JsonInclude.Include.NON_NULL)
    @PostMapping("/notifications")
    public ResponseEntity<HttpStatus> getFitBitNotificationData(@RequestBody String responseFromAPI) {
        System.out.println("////////////////////////  getFitBitNotificationData");
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        try {
            System.out.println("+++++++++  "+responseFromAPI);
            JSONArray jsonArray = new JSONArray(responseFromAPI);
            JSONObject obj = (JSONObject) jsonArray.get(0);
            String subscriptionId = obj.getString("subscriptionId");
            executorService.schedule(() -> {
                deviceService.getDataFromAPIToDB(new DeviceDto(subscriptionId));
            }, 10, TimeUnit.SECONDS);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } finally {
            executorService.shutdown();
        }
    }

    private DeviceRepository deviceRepository;
    private FitbitApi fitbitApi;
    private ModelMapper modelMapper;
    private AuthService authService;

    @GetMapping("/test")
    public String test() {
        System.out.println("****************** test");
        String device = deviceRepository.getDeviceById(UUID.fromString("89873f09-aa05-4cff-988a-e57879de1df0")).toString();
        //System.out.println(device);
        deviceService.getDataFromAPIToDB(new DeviceDto("89873f09-aa05-4cff-988a-e57879de1df0"));
        return device;
    }

    @GetMapping("/sub")
    public Response sub() throws IOException {
        System.out.println("****************** sub");
        DeviceDto device = new DeviceDto("c683d235-6e5b-460d-bb16-642825aef30e");

        Response response = fitbitApi.addSubscription(new FitbitSubscription(device.getId().toString()), authService.getAccessToken(device.dtoToObj(modelMapper)), "activities");
        System.out.println("****************** sub      " + response);

        return fitbitApi.allSubscriptions(authService.getAccessToken(device.dtoToObj(modelMapper)), "activities");
    }

    @GetMapping(value = "/device/patient")
    @ResponseBody
    public Response getPatientDevice(@RequestParam String medicalFileId){
        return deviceService.getPatientDevice(medicalFileId);
    }

}
