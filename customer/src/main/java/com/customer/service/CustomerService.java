package com.customer.service;

import com.clients.fraud.FraudCheckResponse;
import com.clients.fraud.FraudClient;
import com.clients.notification.NotificationClient;
import com.clients.notification.NotificationRequest;
import com.customer.domain.Customer;
import com.customer.dto.CustomerRegistrationRequest;
import com.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public record CustomerService(CustomerRepository customerRepository,
                              RestTemplate restTemplate,
                              FraudClient fraudClient,
                              NotificationClient notificationClient) {
    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();

        customerRepository.saveAndFlush(customer);

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if (fraudCheckResponse.isFraudster())
        {
            throw new IllegalStateException("Fraudster!");
        }

        notificationClient.sendNotification(new NotificationRequest(
                customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome to Kapi...", customer.getFirstName())
        ));
    }
}
