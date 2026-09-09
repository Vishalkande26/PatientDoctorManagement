package com.patientmanagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.entity.Patient;
import com.patientmanagement.service.DoctorPatientService;

@RestController
@RequestMapping("/api/doctor-patient")
public class DoctorPatientController {

    private final DoctorPatientService doctorPatientService;

    public DoctorPatientController(
            DoctorPatientService doctorPatientService) {

        this.doctorPatientService =
                doctorPatientService;
    }

    @PostMapping(
        "/doctors/{doctorId}/patients/{patientId}"
    )
    public ResponseEntity<String> assignDoctorToPatient(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {

        doctorPatientService.assignDoctorToPatient(
                doctorId,
                patientId
        );

        return ResponseEntity.ok(
                "Doctor assigned to patient successfully"
        );
    }

    @DeleteMapping(
        "/doctors/{doctorId}/patients/{patientId}"
    )
    public ResponseEntity<String> removeDoctorFromPatient(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {

        doctorPatientService.removeDoctorFromPatient(
                doctorId,
                patientId
        );

        return ResponseEntity.ok(
                "Doctor removed from patient successfully"
        );
    }

    @GetMapping("/doctors/{doctorId}/patients")
    public ResponseEntity<List<Patient>> getPatientsByDoctor(
            @PathVariable Long doctorId) {

        List<Patient> patients =
                doctorPatientService
                        .getPatientsByDoctor(doctorId);

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patients/{patientId}/doctors")
    public ResponseEntity<List<Doctor>> getDoctorsByPatient(
            @PathVariable Long patientId) {

        List<Doctor> doctors =
                doctorPatientService
                        .getDoctorsByPatient(patientId);

        return ResponseEntity.ok(doctors);
    }
}