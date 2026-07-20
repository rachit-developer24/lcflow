package dev.lcflow.issuance.service;

import dev.lcflow.issuance.model.DocumentType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record IssueLcCommand(
        @NotBlank
        String applicantName,

        @NotBlank
        String beneficiaryName,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotNull
        @Size(min = 3, max = 3)
        String currency,

        @NotNull
        @Future
        LocalDate expiryDate,

        @NotEmpty
        List<DocumentType> requiredDocuments) {
}
