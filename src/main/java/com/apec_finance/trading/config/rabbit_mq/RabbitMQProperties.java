package com.apec_finance.trading.config.rabbit_mq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class RabbitMQProperties {
    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange.order}")
    private String orderExchange;

    @Value("${spring.rabbitmq.exchange.message}")
    private String messageExchange;
}
