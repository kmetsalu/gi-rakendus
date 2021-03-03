package com.kaarelkaasla.backend.services;

import com.kaarelkaasla.backend.entities.VisitDetails;
import com.kaarelkaasla.backend.helpers.VisitDetailsHelper;
import com.kaarelkaasla.backend.repositories.VisitDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class VisitDetailsService {

    private final VisitDetailsRepository visitDetailsRepository;

    @Autowired
    public VisitDetailsService(VisitDetailsRepository visitDetailsRepository) {
        this.visitDetailsRepository = visitDetailsRepository;
    }

    public void save(MultipartFile file) {
        try {
            List<VisitDetails> details = VisitDetailsHelper.visitDetails(file.getInputStream());
            visitDetailsRepository.saveAll(details);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store the CSV data: " +
                    e.getMessage());
        }
    }

    public List<VisitDetails> getAllEntries() {
        return visitDetailsRepository.findAll();
    }

    public List<VisitDetails> getExactEntries(String search) {
        return visitDetailsRepository.findByIdentification(search);
    }

    public List<VisitDetails> getEntriesStartingWith(String search) {
        return visitDetailsRepository.findByIdentificationStartsWith(search);
    }
}
