package ca.uqtr.fitbit;

import ca.uqtr.fitbit.service.activity.ActivityService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
public class FitbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(FitbitApplication.class, args);
    }
    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
    }

}
