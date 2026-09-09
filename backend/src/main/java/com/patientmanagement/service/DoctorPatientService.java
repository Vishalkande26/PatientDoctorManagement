package com.patientmanagement.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.entity.Patient;
import com.patientmanagement.repository.DoctorRepository;
import com.patientmanagement.repository.PatientRepository;

@Service
public class DoctorPatientService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public DoctorPatientService(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public void assignDoctorToPatient(
            Long doctorId,
            Long patientId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with id: "
                                        + doctorId
                        )
                );

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + patientId
                        )
                );

        doctor.getPatients().add(patient);
        patient.getDoctors().add(doctor);

        doctorRepository.save(doctor);
    }

    @Transactional
    public void removeDoctorFromPatient(
            Long doctorId,
            Long patientId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with id: "
                                        + doctorId
                        )
                );

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + patientId
                        )
                );

        doctor.getPatients().remove(patient);
        patient.getDoctors().remove(doctor);

        doctorRepository.save(doctor);
    }

    @Transactional(readOnly = true)
    public List<Patient> getPatientsByDoctor(
            Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Doctor not found with id: "
                                        + doctorId
                        )
                );

        return new ArrayList<>(
                doctor.getPatients()
        );
    }

    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsByPatient(
            Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with id: "
                                        + patientId
                        )
                );

        return new ArrayList<>(
                patient.getDoctors()
        );
    }
}