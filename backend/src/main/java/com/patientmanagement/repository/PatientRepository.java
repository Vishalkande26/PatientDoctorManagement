package com.patientmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Patient;

public interface PatientRepository
        extends JpaRepository<Patient, Long> {

}