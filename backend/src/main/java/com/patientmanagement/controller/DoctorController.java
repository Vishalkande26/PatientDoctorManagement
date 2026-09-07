package com.patientmanagement.controller;

import java.util.List;

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

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.service.DoctorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/doctors")

public class DoctorController {

    private final DoctorService doctorService;


    public DoctorController(
            DoctorService doctorService) {

        this.doctorService =
                doctorService;
    }


    

    @PostMapping
    public ResponseEntity<Doctor> createDoctor(
            @Valid @RequestBody Doctor doctor) {

        Doctor savedDoctor =
                doctorService.createDoctor(doctor);

        return new ResponseEntity<>(
                savedDoctor,
                HttpStatus.CREATED
        );
    }


    

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {

        List<Doctor> doctors =
                doctorService.getAllDoctors();

        return ResponseEntity.ok(
                doctors
        );
    }


    

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(
            @PathVariable Long id) {

        Doctor doctor =
                doctorService.getDoctorById(id);

        return ResponseEntity.ok(
                doctor
        );
    }




    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody Doctor doctor) {

        Doctor updatedDoctor =
                doctorService.updateDoctor(
                        id,
                        doctor
                );

        return ResponseEntity.ok(
                updatedDoctor
        );
    }


    

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDoctor(
            @PathVariable Long id) {

        doctorService.deleteDoctor(id);

        return ResponseEntity.ok(
                "Doctor deleted successfully"
        );
    }
}