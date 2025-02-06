package com.apec_finance.trading.service.impl;

import com.apec_finance.trading.model.otp.CheckOtpRQ;
import com.apec_finance.trading.model.otp.GenOtpRQ;
import com.apec_finance.trading.service.KeycloakService;
import com.apec_finance.trading.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {
    private final RedissonClient redissonClient;
    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRATION_TIME = 2 * 60 * 1000;

    public String generateOtp(Long investorId) {
        String otp = generateRandomOtp();
        RMapCache<Long, String> otpMap = redissonClient.getMapCache("otpMap");
        otpMap.put(investorId, otp, OTP_EXPIRATION_TIME, TimeUnit.SECONDS);

        return otp;
    }

    @Override
    public String getOtp(GenOtpRQ genOtpRQ, Long investorId) {
        String otp = generateOtp(investorId);
        // Send sms to Investor
        log.info("Send sms to phone number: " + genOtpRQ.getPhone());
        return otp;
    }

    @Override
    public boolean validateOtp(CheckOtpRQ rq, Long investorId) {
        RMapCache<Long, String> otpMap = redissonClient.getMapCache("otpMap");
        String storedOtp = otpMap.get(investorId);

        if (storedOtp != null && storedOtp.equals(rq.getOtpInput())) {
            otpMap.remove(investorId);
            return true;
        }

        return false;
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OtpServiceImpl.OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}
