package dev.lcflow.issuance.repository;

import dev.lcflow.issuance.model.LetterOfCredit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LetterOfCreditRepository extends JpaRepository<LetterOfCredit, Long>{
    Optional<LetterOfCredit> findByLcReference(String lcReference);
}
