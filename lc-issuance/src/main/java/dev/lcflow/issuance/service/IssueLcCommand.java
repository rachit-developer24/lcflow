package dev.lcflow.issuance.service;

import dev.lcflow.issuance.model.DocumentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record IssueLcCommand(String applicantName, String beneficiaryName,
                             BigDecimal amount, String currency,
                             LocalDate expiryDate,List<DocumentType>requiredDocuments) {
}
