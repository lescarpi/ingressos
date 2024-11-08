package com.ingressos.api.http.request;

import java.util.List;

public record BookTicketRequest(List<Long> ticketIds) {
}
