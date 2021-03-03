package com.kaarelkaasla.backend.repositories;

import com.kaarelkaasla.backend.entities.VisitDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitDetailsRepository extends JpaRepository<VisitDetails, Long> {

    List<VisitDetails> findByIdentification(String identification);

    List<VisitDetails> findByIdentificationStartsWith(String identification);
}
