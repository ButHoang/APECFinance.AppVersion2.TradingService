package com.apec_finance.trading.config.reddis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
