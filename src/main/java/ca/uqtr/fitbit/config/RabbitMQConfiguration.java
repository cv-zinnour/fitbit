//package ca.uqtr.fitbit.config;
//
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMQConfiguration {
//
//    @Value("${rabbitmq.queue}")
//    private String queue;
//
//    @Value("${rabbitmq.exchange}")
//    private String exchange;
//
//    @Value("${rabbitmq.routingKey}")
//    private String routingKey;
//
//    @Bean
//    Queue queue() {
//        return new Queue(queue, true);
//    }
//
//    @Bean
//    DirectExchange exchange() {
//        return new DirectExchange(exchange);
//    }
//
//    @Bean
//    Binding binding(Queue queue, DirectExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("fish.rmq.cloudamqp.com");
//        connectionFactory.setUsername("bklaoghl");
//        connectionFactory.setPassword("PPdAiJgyoVKYPdNdIdOvMt8F8F7l2B9K");
//        connectionFactory.setVirtualHost("bklaoghl");
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//}
