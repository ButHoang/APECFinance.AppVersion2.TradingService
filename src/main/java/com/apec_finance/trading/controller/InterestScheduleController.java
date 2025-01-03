package com.apec_finance.trading.controller;

import com.apec_finance.trading.comon.ResponseBuilder;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRQ;
import com.apec_finance.trading.model.interest_schedule.InterestScheduleRS;
import com.apec_finance.trading.service.InterestScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interest-schedule")
@RequiredArgsConstructor
public class InterestScheduleController {
    private final InterestScheduleService interestScheduleService;
    @PostMapping("/get")
    public ResponseBuilder<InterestScheduleRS> getInterestSchedule (@RequestBody InterestScheduleRQ rq) {
        InterestScheduleRS rs = interestScheduleService.getInterestSchedule(rq);
        return new ResponseBuilder<>(HttpStatus.OK.value(), "Success", rs);
    }
}
