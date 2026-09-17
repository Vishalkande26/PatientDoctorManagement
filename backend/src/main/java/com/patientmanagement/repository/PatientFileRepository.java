package com.patientmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.PatientFile;

public interface PatientFileRepository
        extends JpaRepository<PatientFile, Long> {

    List<PatientFile>
    findByPatientIdOrderByUploadedAtDesc(
            Long patientId
    );

    Optional<PatientFile>
    findByIdAndPatientId(
            Long id,
            Long patientId
    );
}