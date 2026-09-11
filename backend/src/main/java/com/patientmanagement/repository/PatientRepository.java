package com.patientmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    /*
     * Get only active patients.
     */
    List<Patient> findByDeletedFalse();

    /*
     * Get patient only when it is active.
     */
    Optional<Patient> findByIdAndDeletedFalse(Long id);
}