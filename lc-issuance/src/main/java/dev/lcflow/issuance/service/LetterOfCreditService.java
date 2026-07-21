package dev.lcflow.issuance.service;

import dev.lcflow.issuance.model.LcStatus;
import dev.lcflow.issuance.model.LetterOfCredit;
import dev.lcflow.issuance.repository.LetterOfCreditRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.UUID;

@Service
public class LetterOfCreditService {

    private final LetterOfCreditRepository letterOfCreditRepository;

    public LetterOfCreditService(LetterOfCreditRepository letterOfCreditRepository) {
        this.letterOfCreditRepository = letterOfCreditRepository;
    }

    private String generateLcReference() {
        long next = letterOfCreditRepository.nextLcReferenceNumber();
        return String.format("LC-%d-%06d", Year.now().getValue(), next);
    }

    @Transactional
    public LetterOfCredit issue(IssueLcCommand command) {
        LetterOfCredit letterOfCredit = new LetterOfCredit();
        letterOfCredit.setApplicantName(command.applicantName());

        letterOfCredit.setBeneficiaryName(command.beneficiaryName());

        letterOfCredit.setAmount(command.amount());

        letterOfCredit.setCurrency(command.currency());

        letterOfCredit.setExpiryDate(command.expiryDate());

        letterOfCredit.setRequiredDocuments(command.requiredDocuments());

        letterOfCredit.setLcReference(generateLcReference());

        letterOfCredit.setStatus(LcStatus.ISSUED);

        letterOfCredit.setCreatedAt(Instant.now());

        return letterOfCreditRepository.save(letterOfCredit);
    }
}
