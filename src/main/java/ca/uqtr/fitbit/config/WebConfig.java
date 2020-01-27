package ca.uqtr.fitbit.config;

import ca.uqtr.fitbit.api.LoggingInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig  {



    @Bean
    OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
    }



}
