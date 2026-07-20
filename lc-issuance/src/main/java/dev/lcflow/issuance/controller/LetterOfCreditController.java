package dev.lcflow.issuance.controller;

import dev.lcflow.issuance.model.LetterOfCredit;
import dev.lcflow.issuance.service.IssueLcCommand;
import dev.lcflow.issuance.service.LetterOfCreditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lcs")
public class LetterOfCreditController {

    private final LetterOfCreditService letterOfCreditService;

    public LetterOfCreditController(LetterOfCreditService letterOfCreditService) {
        this.letterOfCreditService = letterOfCreditService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueLcResponse issue(@Valid @RequestBody IssueLcCommand command) {
        LetterOfCredit letterOfCredit = letterOfCreditService.issue(command);
        return new IssueLcResponse(letterOfCredit.getLcReference(),
                letterOfCredit.getStatus(),
                letterOfCredit.getCreatedAt());

    }
}
