package com.patientmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patientmanagement.entity.Patient;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.AppointmentRepository;
import com.patientmanagement.repository.PatientRepository;

@Service
public class PatientService {

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


    public Patient createPatient(
            Patient patient) {

        return patientRepository.save(
                patient
        );
    }


    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }


    public Patient getPatientById(
            Long id) {

        return patientRepository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Patient not found with id: " + id
                        )
                );
    }


    public Patient updatePatient(
            Long id,
            Patient patient) {

        Patient existingPatient =
                patientRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Patient not found with id: " + id
                                )
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


        return patientRepository.save(
                existingPatient
        );
    }


    @Transactional
    public void deletePatient(
            Long id) {

        Patient patient =
                patientRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Patient not found with id: " + id
                                )
                        );


        /*
         * Delete appointments first.
         * appointments.patient_id references patients.id.
         */
        appointmentRepository
                .deleteByPatient_Id(id);


        /*
         * Now delete patient.
         */
        patientRepository.delete(
                patient
        );
    }
}