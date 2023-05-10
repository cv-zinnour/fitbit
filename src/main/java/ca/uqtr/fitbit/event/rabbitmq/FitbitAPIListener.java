package ca.uqtr.fitbit.event.rabbitmq;


import ca.uqtr.fitbit.FitbitAPIException;
import ca.uqtr.fitbit.dto.DeviceDto;
import ca.uqtr.fitbit.dto.Response;
import ca.uqtr.fitbit.entity.fitbit.Message;
import ca.uqtr.fitbit.repository.DeviceRepository;
import ca.uqtr.fitbit.service.device.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class FitbitAPIListener {
    private static final java.util.logging.Logger LOGGER = Logger.getLogger( FitbitAPIListener.class.getName() );
    private ObjectMapper mapper;
    private DeviceService deviceService;
    private AmqpTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingkey;
    private final String queue = "fitbit-queue";
    private final String deadLetterQueue = "fitbit-dlq";
    private DeviceRepository deviceRepository;

    @Autowired
    public FitbitAPIListener(DeviceRepository deviceRepository, ObjectMapper mapper, DeviceService deviceService, RabbitTemplate rabbitTemplate) {
        this.mapper = mapper;
        this.deviceService = deviceService;
        this.rabbitTemplate = rabbitTemplate;
        this.deviceRepository = deviceRepository;

    }

    @RabbitListener(queues = queue)
    public void receiveMessage(Message message) throws FitbitAPIException {
        LOGGER.info("Received message : "+ message.toString());
        System.out.println(message.getSubscriptionId());
        deviceService.getDataFromAPIToDB(new DeviceDto(message.getSubscriptionId()));
    }

    @RabbitListener(queues = deadLetterQueue)
    public void processFailedMessages(org.springframework.amqp.core.Message message) {
        LOGGER.info("Received failed message : "+ message.toString());
        rabbitTemplate.convertAndSend(exchange, message.getMessageProperties().getReceivedRoutingKey(), message);
    }

}
