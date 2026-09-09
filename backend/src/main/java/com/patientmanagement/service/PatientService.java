package com.patientmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patientmanagement.entity.Patient;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.AppointmentRepository;
import com.patientmanagement.repository.PatientRepository;

@Service
public class PatientService {

    private static final Logger logger =
            LoggerFactory.getLogger(PatientService.class);

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {

        this.patientRepository =
                patientRepository;

        this.appointmentRepository =
                appointmentRepository;
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

        Patient savedPatient =
                patientRepository.save(patient);

        logger.info(
                "Patient created successfully with id: {}",
                savedPatient.getId()
        );

        return savedPatient;
    }

    // ==============================
    // GET ALL PATIENTS
    // ==============================

    public List<Patient> getAllPatients() {

        logger.info("Fetching all patients");

        List<Patient> patients =
                patientRepository.findAll();

        logger.info(
                "Found {} patients",
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
                "Fetching patient with id: {}",
                id
        );

        return patientRepository
                .findById(id)
                .orElseThrow(
                        () -> {

                            logger.warn(
                                    "Patient not found with id: {}",
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
                        .findById(id)
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
    // DELETE PATIENT
    // ==============================

    @Transactional
    public void deletePatient(
            Long id) {

        logger.info(
                "Starting deletion of patient with id: {}",
                id
        );

        Patient patient =
                patientRepository
                        .findById(id)
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
         * Delete appointments first.
         * appointments.patient_id references patients.id.
         */
        logger.info(
                "Deleting appointments for patient with id: {}",
                id
        );

        appointmentRepository
                .deleteByPatient_Id(id);

        /*
         * Now delete patient.
         */
        patientRepository.delete(
                patient
        );

        logger.info(
                "Patient deleted successfully with id: {}",
                id
        );
    }
}