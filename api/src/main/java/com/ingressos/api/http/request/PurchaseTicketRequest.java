package com.ingressos.api.http.request;

import java.util.List;

public record PurchaseTicketRequest(List<Long> ticketIds) {
}