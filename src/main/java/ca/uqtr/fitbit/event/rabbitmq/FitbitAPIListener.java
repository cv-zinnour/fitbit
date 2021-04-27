package ca.uqtr.fitbit.event.rabbitmq;


import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.entity.fitbit.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FitbitAPIListener {
    private static final Logger log = LoggerFactory.getLogger(FitbitAPIListener.class);
    private ObjectMapper mapper;

    @Autowired
    public FitbitAPIListener(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @RabbitListener(queues = "fitbit-queue")
    public void receiveMessage(Message payload) {
        log.info("Received payload : {}", payload.toString());
        Message message = mapper.convertValue(payload, Message.class);
        log.info("Received message : {}", message.toString());
    }


}
