package ca.uqtr.fitbit;

import ca.uqtr.fitbit.service.activity.ActivityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
public class FitbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitbitApplication.class, args);
    }

}
