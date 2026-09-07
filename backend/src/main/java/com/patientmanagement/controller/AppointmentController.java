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

import com.patientmanagement.dto.AppointmentRequestDTO;
import com.patientmanagement.dto.AppointmentResponseDTO;
import com.patientmanagement.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")

public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(
            AppointmentService appointmentService) {

        this.appointmentService = appointmentService;
    }


    
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> createAppointment(
            @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO response =
                appointmentService.createAppointment(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }


    
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDTO>>
            getAllAppointments() {

        List<AppointmentResponseDTO> appointments =
                appointmentService.getAllAppointments();

        return ResponseEntity.ok(appointments);
    }



    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO>
            getAppointmentById(
                    @PathVariable Long id) {

        AppointmentResponseDTO appointment =
                appointmentService.getAppointmentById(id);

        return ResponseEntity.ok(appointment);
    }



    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO>
            updateAppointment(
                    @PathVariable Long id,
                    @RequestBody AppointmentRequestDTO request) {

        AppointmentResponseDTO updatedAppointment =
                appointmentService.updateAppointment(
                        id,
                        request
                );

        return ResponseEntity.ok(updatedAppointment);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable Long id) {

        appointmentService.deleteAppointment(id);

        return ResponseEntity.ok(
                "Appointment deleted successfully"
        );
    }
}