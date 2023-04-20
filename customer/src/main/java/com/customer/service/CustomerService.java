package com.customer.service;

import com.customer.domain.Customer;
import com.customer.dto.CustomerRegistrationRequest;
import com.customer.dto.FraudCheckResponse;
import com.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              RestTemplate restTemplate) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject("http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class, customer.getId());

        if (fraudCheckResponse.isFraudster())
        {
            throw new IllegalStateException("Fraudster!");
        }
    }
}
