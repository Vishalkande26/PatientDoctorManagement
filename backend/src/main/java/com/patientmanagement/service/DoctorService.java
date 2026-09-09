package com.patientmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // ==============================
    // CREATE DOCTOR
    // ==============================

    public Doctor createDoctor(Doctor doctor) {

        // New doctors are active by default
        doctor.setDeleted(false);

        return doctorRepository.save(doctor);
    }

    // ==============================
    // GET ALL ACTIVE DOCTORS
    // ==============================

    public List<Doctor> getAllDoctors() {

        return doctorRepository.findByDeletedFalse();
    }

    // ==============================
    // GET ACTIVE DOCTOR BY ID
    // ==============================

    public Doctor getDoctorById(Long id) {

        return doctorRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: " + id
                        )
                );
    }

    // ==============================
    // UPDATE DOCTOR
    // ==============================

    public Doctor updateDoctor(
            Long id,
            Doctor doctor) {

        Doctor existingDoctor =
                doctorRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: " + id
                                )
                        );

        existingDoctor.setName(
                doctor.getName()
        );

        existingDoctor.setSpecialization(
                doctor.getSpecialization()
        );

        existingDoctor.setPhone(
                doctor.getPhone()
        );

        existingDoctor.setEmail(
                doctor.getEmail()
        );

        existingDoctor.setExperience(
                doctor.getExperience()
        );

        return doctorRepository.save(existingDoctor);
    }

    // ==============================
    // SOFT DELETE DOCTOR
    // ==============================

    public void deleteDoctor(Long id) {

        Doctor existingDoctor =
                doctorRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: " + id
                                )
                        );

        // Soft delete:
        // false = active
        // true  = deleted
        existingDoctor.setDeleted(true);

        doctorRepository.save(existingDoctor);
    }
}