package com.apec_finance.trading.config.reddis;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Data
public class RedissonClientConfig {
    private final RedisProperties redisProperties;
    private String host;
    private int port;
    private String username;
    private String password;

    Config config = new Config();

    @Bean
    RedissonClient redissonClient() {
        config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());

        if (redisProperties.getUsername() != null && !redisProperties.getUsername().trim().isEmpty()) {
            config.useSingleServer().setUsername(redisProperties.getUsername());
        }

        if (redisProperties.getPassword() != null && !redisProperties.getPassword().trim().isEmpty()) {
            config.useSingleServer().setPassword(redisProperties.getPassword());
        }
        config.setCodec(new StringCodec()).useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort());
        return Redisson.create(config);
    }
}


