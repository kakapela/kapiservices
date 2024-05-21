package com.clients.notification;

import java.io.Serializable;

public record NotificationRequest(
        Integer toCustomerId,
        String toCustomerName,
        String message
) implements Serializable {
}