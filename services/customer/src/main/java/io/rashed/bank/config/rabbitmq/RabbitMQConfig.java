package io.rashed.bank.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "customer.deleted.queue";
    public static final String EXCHANGE_NAME = "customer.exchange";
    public static final String CUSTOMER_DELETE_QUEUE_ROUTING_KEY = "customer.deleted.#";


    @Bean
    public Queue customerDeletedQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public TopicExchange customerExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue customerDeletedQueue, TopicExchange customerExchange) {
        return BindingBuilder.bind(customerDeletedQueue)
                .to(customerExchange)
                .with(CUSTOMER_DELETE_QUEUE_ROUTING_KEY);
    }
}
