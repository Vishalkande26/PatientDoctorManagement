package com.patientmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Doctor;

public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {
}