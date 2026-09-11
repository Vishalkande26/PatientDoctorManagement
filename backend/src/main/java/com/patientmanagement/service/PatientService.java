package com.patientmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.patientmanagement.entity.Patient;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.PatientRepository;

@Service
public class PatientService {

    private static final Logger logger =
            LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    public PatientService(
            PatientRepository patientRepository) {

        this.patientRepository =
                patientRepository;
    }

    // ==============================
    // CREATE PATIENT
    // ==============================

    public Patient createPatient(
            Patient patient) {

        logger.info(
                "Creating patient with name: {}",
                patient.getName()
        );

        /*
         * New patients are always active.
         */
        patient.setDeleted(false);

        Patient savedPatient =
                patientRepository.save(patient);

        logger.info(
                "Patient created successfully with id: {}",
                savedPatient.getId()
        );

        return savedPatient;
    }

    // ==============================
    // GET ALL ACTIVE PATIENTS
    // ==============================

    public List<Patient> getAllPatients() {

        logger.info("Fetching all active patients");

        List<Patient> patients =
                patientRepository.findByDeletedFalse();

        logger.info(
                "Found {} active patients",
                patients.size()
        );

        return patients;
    }

    // ==============================
    // GET PATIENT BY ID
    // ==============================

    public Patient getPatientById(
            Long id) {

        logger.info(
                "Fetching active patient with id: {}",
                id
        );

        return patientRepository
                .findByIdAndDeletedFalse(id)
                .orElseThrow(
                        () -> {

                            logger.warn(
                                    "Active patient not found with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Patient not found with id: " + id
                            );
                        }
                );
    }

    // ==============================
    // UPDATE PATIENT
    // ==============================

    public Patient updatePatient(
            Long id,
            Patient patient) {

        logger.info(
                "Updating patient with id: {}",
                id
        );

        Patient existingPatient =
                patientRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(
                                () -> {

                                    logger.warn(
                                            "Patient not found for update with id: {}",
                                            id
                                    );

                                    return new ResourceNotFoundException(
                                            "Patient not found with id: " + id
                                    );
                                }
                        );

        existingPatient.setName(
                patient.getName()
        );

        existingPatient.setAge(
                patient.getAge()
        );

        existingPatient.setGender(
                patient.getGender()
        );

        existingPatient.setPhone(
                patient.getPhone()
        );

        existingPatient.setDisease(
                patient.getDisease()
        );

        existingPatient.setAddress(
                patient.getAddress()
        );

        /*
         * Do not allow the update request to
         * change the soft-delete status.
         */
        existingPatient.setDeleted(false);

        Patient updatedPatient =
                patientRepository.save(
                        existingPatient
                );

        logger.info(
                "Patient updated successfully with id: {}",
                id
        );

        return updatedPatient;
    }

    // ==============================
    // SOFT DELETE PATIENT
    // ==============================

    public void deletePatient(
            Long id) {

        logger.info(
                "Starting soft deletion of patient with id: {}",
                id
        );

        Patient patient =
                patientRepository
                        .findByIdAndDeletedFalse(id)
                        .orElseThrow(
                                () -> {

                                    logger.warn(
                                            "Patient not found for deletion with id: {}",
                                            id
                                    );

                                    return new ResourceNotFoundException(
                                            "Patient not found with id: " + id
                                    );
                                }
                        );

        /*
         * Soft delete:
         *
         * The patient row remains in the database.
         * Only deleted changes from 0 to 1.
         */
        patient.setDeleted(true);

        patientRepository.save(patient);

        logger.info(
                "Patient soft deleted successfully with id: {}",
                id
        );
    }
}