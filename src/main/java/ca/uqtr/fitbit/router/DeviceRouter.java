package ca.uqtr.fitbit.router;

import ca.uqtr.fitbit.handler.DeviceRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class DeviceRouter {

    @Bean
    RouterFunction<ServerResponse> routes(DeviceRequestHandler deviceRequestHandler) {
        return RouterFunctions
                .route(RequestPredicates
                                .GET("/device/all/not/returned"),
                        deviceRequestHandler::streamWeather);
    }
}
