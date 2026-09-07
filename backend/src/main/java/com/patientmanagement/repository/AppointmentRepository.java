package com.patientmanagement.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.patientmanagement.entity.Appointment;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

    boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndIdNot(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime,
            Long id
    );

    void deleteByPatient_Id(
            Long patientId
    );
}