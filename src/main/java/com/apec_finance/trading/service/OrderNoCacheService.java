package com.apec_finance.trading.service;

import com.apec_finance.trading.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderNoCacheService {

    private static final String CACHE_NAME = "orderNoCache";

    private final OrderRepository orderRepository;

    @Cacheable(value = CACHE_NAME, key = "'orderNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd'))")
    public int getOrderNoSequence() {
        LocalDate today = LocalDate.now();
        String orderNoPrefix = "O" + today.format(DateTimeFormatter.ofPattern("yyMMdd"));

        long orderCount = orderRepository.countOrdersByPrefix(orderNoPrefix);

        return (int) orderCount;
    }

    @CacheEvict(value = CACHE_NAME, key = "'orderNo_' + T(java.time.LocalDate).now().format(T(java.time.format.DateTimeFormatter).ofPattern('yyyy-MM-dd'))")
    public int generateOrderNo() {
        int currentSequence = getOrderNoSequence();
        currentSequence++;
        return currentSequence;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCacheAtMidnight() {
        System.out.println("Cache has been reset at midnight!");
    }
}


