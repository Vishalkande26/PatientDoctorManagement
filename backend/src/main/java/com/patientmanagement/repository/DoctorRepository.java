package com.patientmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Get only active doctors
    List<Doctor> findByDeletedFalse();

    // Get one doctor only if it is active
    Optional<Doctor> findByIdAndDeletedFalse(Long id);
}