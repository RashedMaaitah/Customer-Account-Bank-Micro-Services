package io.rashed.bank.listener;

import io.rashed.bank.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {

    private final AccountService accountService;

    @RabbitListener(queues = "customer.deleted.queue")
    public void listen(Long customerId) {
        accountService.deleteCustomerAccounts(customerId);
    }
}
