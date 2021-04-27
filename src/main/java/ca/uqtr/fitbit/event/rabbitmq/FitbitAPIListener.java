package ca.uqtr.fitbit.event.rabbitmq;


import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.fitbit.Message;
import ca.uqtr.fitbit.service.device.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FitbitAPIListener {
    private static final Logger log = LoggerFactory.getLogger(FitbitAPIListener.class);
    private ObjectMapper mapper;
    private DeviceService deviceService;

    @Autowired
    public FitbitAPIListener(ObjectMapper mapper, DeviceService deviceService) {
        this.mapper = mapper;
        this.deviceService = deviceService;
    }

    @RabbitListener(queues = "fitbit-queue")
    public void receiveMessage(Message message) {
        log.info("Received message : {}", message.toString());
        //deviceService.getDataFromAPIToDB(new DeviceDto(message.getSubscriptionId()));
    }


}
