package dev.lcflow.issuance.controller;

import dev.lcflow.issuance.model.LcStatus;

import java.time.Instant;

public record IssueLcResponse(
        String lcReference,
        LcStatus status,
        Instant createdAt
) {
}
