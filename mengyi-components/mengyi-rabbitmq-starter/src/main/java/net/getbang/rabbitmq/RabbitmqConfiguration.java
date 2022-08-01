package net.getbang.rabbitmq;

import net.getbang.rabbitmq.properties.RabbitmqProperties;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(ConsumerRunCondition.class)
public class RabbitmqConfiguration {


    @Autowired
    private RabbitmqProperties rabbitmqProperties;

    public static final String EXCHANGE = "amq.direct";
    public static final String EXCHANGE_DELAY = "exchange.delayed";

    @Bean
    public ConnectionFactory rabbitmqConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(rabbitmqProperties.getAddress());
        connectionFactory.setUsername(rabbitmqProperties.getUserName());
        connectionFactory.setPassword(rabbitmqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitmqProperties.getVirtualHost());
        connectionFactory.setPublisherConfirms(false);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(rabbitmqConnectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
