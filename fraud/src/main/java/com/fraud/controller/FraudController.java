package com.fraud.controller;

import com.fraud.dto.FraudCheckResponse;
import com.fraud.service.FraudCheckService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
public class FraudController {
    private final FraudCheckService fraudCheckService;

    @GetMapping("{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Integer customerId) {
        boolean fraudelentCustomer = fraudCheckService.isFraudelentCustomer(customerId);
        return new FraudCheckResponse(fraudelentCustomer);
    }
}
