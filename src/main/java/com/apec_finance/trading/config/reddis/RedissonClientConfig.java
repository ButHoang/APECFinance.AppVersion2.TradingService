package com.apec_finance.trading.config.reddis;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonClientConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.username}")
    private String redisUserName;

    @Value("${spring.redis.password}")
    private String redisPassword;

    Config config = new Config();

    @Bean
    RedissonClient redissonClient() {
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort)
                .setUsername(redisUserName)
                .setPassword(redisPassword);
        config.setCodec(new StringCodec()).useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }
}


