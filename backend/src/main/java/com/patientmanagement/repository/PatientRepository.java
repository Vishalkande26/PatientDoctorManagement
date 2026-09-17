package com.patientmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByDeletedFalse();

    Optional<Patient> findByIdAndDeletedFalse(Long id);

    Optional<Patient> findByUserId(Long userId);
}