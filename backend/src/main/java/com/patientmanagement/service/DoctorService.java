package com.patientmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.DoctorRepository;

@Service
public class DoctorService {

    private static final Logger logger =
            LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // ==============================
    // CREATE DOCTOR
    // ==============================

    public Doctor createDoctor(Doctor doctor) {

        logger.info("Creating doctor with name: {}", doctor.getName());

        doctor.setDeleted(false);

        Doctor savedDoctor = doctorRepository.save(doctor);

        logger.info(
                "Doctor created successfully with id: {}",
                savedDoctor.getId()
        );

        return savedDoctor;
    }

    // ==============================
    // GET ALL ACTIVE DOCTORS
    // ==============================

    public List<Doctor> getAllDoctors() {

        logger.info("Fetching all active doctors");

        List<Doctor> doctors =
                doctorRepository.findByDeletedFalse();

        logger.info(
                "Found {} active doctors",
                doctors.size()
        );

        return doctors;
    }

    // ==============================
    // GET ACTIVE DOCTOR BY ID
    // ==============================

    public Doctor getDoctorById(Long id) {

        logger.info("Fetching doctor with id: {}", id);

        return doctorRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {

                    logger.warn(
                            "Doctor not found with id: {}",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Doctor not found with id: " + id
                    );
                });
    }

    // ==============================
    // UPDATE DOCTOR
    // ==============================

    public Doctor updateDoctor(
            Long id,
            Doctor doctor) {

        logger.info(
                "Updating doctor with id: {}",
                id
        );

        Doctor existingDoctor =
                doctorRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Doctor not found for update with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Doctor not found with id: " + id
                            );
                        });

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

        Doctor updatedDoctor =
                doctorRepository.save(existingDoctor);

        logger.info(
                "Doctor updated successfully with id: {}",
                id
        );

        return updatedDoctor;
    }

    // ==============================
    // SOFT DELETE DOCTOR
    // ==============================

    public void deleteDoctor(Long id) {

        logger.info(
                "Soft deleting doctor with id: {}",
                id
        );

        Doctor existingDoctor =
                doctorRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Doctor not found for deletion with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Doctor not found with id: " + id
                            );
                        });

        existingDoctor.setDeleted(true);

        doctorRepository.save(existingDoctor);

        logger.info(
                "Doctor with id {} was soft deleted successfully",
                id
        );
    }
}