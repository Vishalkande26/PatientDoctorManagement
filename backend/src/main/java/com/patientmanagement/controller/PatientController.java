package com.patientmanagement.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.patientmanagement.entity.Patient;
import com.patientmanagement.service.PatientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private static final Logger logger =
            LoggerFactory.getLogger(PatientController.class);

    private final PatientService patientService;

    public PatientController(
            PatientService patientService) {

        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(
            @Valid @RequestBody Patient patient) {

        logger.info(
                "POST /api/patients - Creating patient"
        );

        Patient savedPatient =
                patientService.createPatient(patient);

        return new ResponseEntity<>(
                savedPatient,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {

        logger.info(
                "GET /api/patients - Fetching all patients"
        );

        List<Patient> patients =
                patientService.getAllPatients();

        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @PathVariable Long id) {

        logger.info(
                "GET /api/patients/{} - Fetching patient",
                id
        );

        Patient patient =
                patientService.getPatientById(id);

        return ResponseEntity.ok(patient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody Patient patient) {

        logger.info(
                "PUT /api/patients/{} - Updating patient",
                id
        );

        Patient updatedPatient =
                patientService.updatePatient(
                        id,
                        patient
                );

        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(
            @PathVariable Long id) {

        logger.info(
                "DELETE /api/patients/{} - Deleting patient",
                id
        );

        patientService.deletePatient(id);

        return ResponseEntity.ok(
                "Patient deleted successfully"
        );
    }
}