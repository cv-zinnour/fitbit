package ca.uqtr.fitbit.config;

import ca.uqtr.fitbit.api.LoggingInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import okhttp3.OkHttpClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class Config {



    @Bean
    OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor())
                .build();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("messages/error");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }

    @Bean
    public ResourceBundleMessageSource messageResourceSource() {

        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("classpath:messages");
        source.setUseCodeAsDefaultMessage(true);

        return source;
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
