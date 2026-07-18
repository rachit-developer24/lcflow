package dev.lcflow.issuance.service;

import dev.lcflow.issuance.repository.LetterOfCreditRepository;
import org.springframework.stereotype.Service;

@Service
public class LetterOfCreditService {

    private final LetterOfCreditRepository letterOfCreditRepository;

    public LetterOfCreditService(LetterOfCreditRepository letterOfCreditRepository){
        this.letterOfCreditRepository = letterOfCreditRepository;
    }

}
