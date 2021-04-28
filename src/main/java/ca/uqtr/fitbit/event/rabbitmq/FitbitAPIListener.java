package ca.uqtr.fitbit.event.rabbitmq;


import ca.uqtr.fitbit.FitbitAPIException;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.Message;
import ca.uqtr.fitbit.service.device.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FitbitAPIListener {
    private static final Logger log = LoggerFactory.getLogger(FitbitAPIListener.class);
    private ObjectMapper mapper;
    private DeviceService deviceService;
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingkey;
    private final String queue = "fitbit-queue";
    private final String deadLetterQueue = "fitbit-dlq";

    @Autowired
    public FitbitAPIListener(ObjectMapper mapper, DeviceService deviceService, RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.deviceService = deviceService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = queue)
    public void receiveMessage(Message message) throws FitbitAPIException {
        log.info("Received message : {}", message.toString());
        Response response = deviceService.getDataFromAPIToDB(new DeviceDto(message.getSubscriptionId()));
        if (response.getObject() == null)
            throw new FitbitAPIException();
    }

    @RabbitListener(queues = deadLetterQueue)
    public void processFailedMessages(org.springframework.amqp.core.Message message) {
        log.info("Received failed message: {}", message.toString());
        rabbitTemplate.convertAndSend(exchange, message.getMessageProperties().getReceivedRoutingKey(), message);
    }

}
