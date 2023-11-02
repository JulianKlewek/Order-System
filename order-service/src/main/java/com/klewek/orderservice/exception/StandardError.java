package com.klewek.orderservice.exception;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record StandardError(Instant timestamp, int status, String error, List<String> detailedMessage, String path) {
}
