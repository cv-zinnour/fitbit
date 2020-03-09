package ca.uqtr.fitbit.handler;

import ca.uqtr.fitbit.service.device.DeviceService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DeviceRequestHandler {


    private DeviceService deviceService;

    public DeviceRequestHandler(DeviceService deviceService) {
        this.deviceService = deviceService;
    }


    public Mono<ServerResponse> streamWeather(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(BodyInserters.fromObject(deviceService.getDevicesNotReturned()));
    }
}
