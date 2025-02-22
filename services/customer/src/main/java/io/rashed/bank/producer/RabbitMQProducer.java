package io.rashed.bank.producer;

import io.rashed.bank.config.rabbitmq.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static io.rashed.bank.config.rabbitmq.RabbitMQConfig.CUSTOMER_DELETE_QUEUE_ROUTING_KEY;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendDeleteCustomerAccounts(Long customerId) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                CUSTOMER_DELETE_QUEUE_ROUTING_KEY,
                customerId
        );
    }
}
