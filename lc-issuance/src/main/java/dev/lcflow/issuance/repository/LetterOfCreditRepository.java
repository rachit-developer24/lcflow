package dev.lcflow.issuance.repository;

import dev.lcflow.issuance.model.LetterOfCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


public interface LetterOfCreditRepository extends JpaRepository<LetterOfCredit, Long>{
    Optional<LetterOfCredit> findByLcReference(String lcReference);

    @Query(value = "SELECT nextval('lc_reference_seq')", nativeQuery = true)
    long nextLcReferenceNumber();
}

