package com.patientmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.patientmanagement.dto.AppointmentRequestDTO;
import com.patientmanagement.dto.AppointmentResponseDTO;
import com.patientmanagement.entity.Appointment;
import com.patientmanagement.entity.Doctor;
import com.patientmanagement.entity.Patient;
import com.patientmanagement.exception.ConflictException;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.AppointmentRepository;
import com.patientmanagement.repository.DoctorRepository;
import com.patientmanagement.repository.PatientRepository;

@Service
public class AppointmentService {

    private static final Logger logger =
            LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository) {

        this.appointmentRepository =
                appointmentRepository;

        this.patientRepository =
                patientRepository;

        this.doctorRepository =
                doctorRepository;
    }

    // ==============================
    // CREATE APPOINTMENT
    // ==============================

    public AppointmentResponseDTO createAppointment(
            AppointmentRequestDTO request) {

        logger.info(
                "Creating appointment for patient id: {} and doctor id: {}",
                request.getPatientId(),
                request.getDoctorId()
        );

        boolean duplicate =
                appointmentRepository
                        .existsByDoctor_IdAndAppointmentDateAndAppointmentTime(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (duplicate) {

            logger.warn(
                    "Duplicate appointment found for doctor id: {} on date: {} at time: {}",
                    request.getDoctorId(),
                    request.getAppointmentDate(),
                    request.getAppointmentTime()
            );

            throw new ConflictException(
                    "Appointment already exists for this doctor, date and time."
            );
        }

        Patient patient =
                patientRepository
                        .findById(
                                request.getPatientId()
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Patient not found with id: {}",
                                    request.getPatientId()
                            );

                            return new ResourceNotFoundException(
                                    "Patient not found with id: "
                                            + request.getPatientId()
                            );
                        });

        Doctor doctor =
                doctorRepository
                        .findById(
                                request.getDoctorId()
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Doctor not found with id: {}",
                                    request.getDoctorId()
                            );

                            return new ResourceNotFoundException(
                                    "Doctor not found with id: "
                                            + request.getDoctorId()
                            );
                        });

        Appointment appointment =
                new Appointment();

        appointment.setPatient(
                patient
        );

        appointment.setDoctor(
                doctor
        );

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setStatus(
                request.getStatus()
        );

        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment
                );

        logger.info(
                "Appointment created successfully with id: {}",
                savedAppointment.getId()
        );

        return convertToResponse(
                savedAppointment
        );
    }

    // ==============================
    // GET ALL APPOINTMENTS
    // ==============================

    public List<AppointmentResponseDTO>
    getAllAppointments() {

        logger.info("Fetching all appointments");

        List<AppointmentResponseDTO> appointments =
                appointmentRepository
                        .findAll()
                        .stream()
                        .map(this::convertToResponse)
                        .toList();

        logger.info(
                "Found {} appointments",
                appointments.size()
        );

        return appointments;
    }

    // ==============================
    // GET APPOINTMENT BY ID
    // ==============================

    public AppointmentResponseDTO
    getAppointmentById(
            Long id) {

        logger.info(
                "Fetching appointment with id: {}",
                id
        );

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Appointment not found with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Appointment not found with id: "
                                            + id
                            );
                        });

        return convertToResponse(
                appointment
        );
    }

    // ==============================
    // UPDATE APPOINTMENT
    // ==============================

    public AppointmentResponseDTO
    updateAppointment(
            Long id,
            AppointmentRequestDTO request) {

        logger.info(
                "Updating appointment with id: {}",
                id
        );

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Appointment not found for update with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Appointment not found with id: "
                                            + id
                            );
                        });

        boolean duplicate =
                appointmentRepository
                        .existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndIdNot(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                id
                        );

        if (duplicate) {

            logger.warn(
                    "Duplicate appointment found while updating appointment id: {}",
                    id
            );

            throw new ConflictException(
                    "Appointment already exists for this doctor, date and time."
            );
        }

        Patient patient =
                patientRepository
                        .findById(
                                request.getPatientId()
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Patient not found with id: {} during appointment update",
                                    request.getPatientId()
                            );

                            return new ResourceNotFoundException(
                                    "Patient not found with id: "
                                            + request.getPatientId()
                            );
                        });

        Doctor doctor =
                doctorRepository
                        .findById(
                                request.getDoctorId()
                        )
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Doctor not found with id: {} during appointment update",
                                    request.getDoctorId()
                            );

                            return new ResourceNotFoundException(
                                    "Doctor not found with id: "
                                            + request.getDoctorId()
                            );
                        });

        appointment.setPatient(
                patient
        );

        appointment.setDoctor(
                doctor
        );

        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        appointment.setAppointmentTime(
                request.getAppointmentTime()
        );

        appointment.setReason(
                request.getReason()
        );

        appointment.setStatus(
                request.getStatus()
        );

        Appointment updatedAppointment =
                appointmentRepository.save(
                        appointment
                );

        logger.info(
                "Appointment updated successfully with id: {}",
                id
        );

        return convertToResponse(
                updatedAppointment
        );
    }

    // ==============================
    // DELETE APPOINTMENT
    // ==============================

    public void deleteAppointment(
            Long id) {

        logger.info(
                "Deleting appointment with id: {}",
                id
        );

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() -> {

                            logger.warn(
                                    "Appointment not found for deletion with id: {}",
                                    id
                            );

                            return new ResourceNotFoundException(
                                    "Appointment not found with id: "
                                            + id
                            );
                        });

        appointmentRepository.delete(
                appointment
        );

        logger.info(
                "Appointment deleted successfully with id: {}",
                id
        );
    }

    // ==============================
    // CONVERT TO RESPONSE DTO
    // ==============================

    private AppointmentResponseDTO
    convertToResponse(
            Appointment appointment) {

        AppointmentResponseDTO response =
                new AppointmentResponseDTO();

        response.setId(
                appointment.getId()
        );

        response.setPatientId(
                appointment
                        .getPatient()
                        .getId()
        );

        response.setPatientName(
                appointment
                        .getPatient()
                        .getName()
        );

        response.setPatientDisease(
                appointment
                        .getPatient()
                        .getDisease()
        );

        response.setPatientAddress(
                appointment
                        .getPatient()
                        .getAddress()
        );

        response.setDoctorId(
                appointment
                        .getDoctor()
                        .getId()
        );

        response.setDoctorName(
                appointment
                        .getDoctor()
                        .getName()
        );

        response.setDoctorSpecialization(
                appointment
                        .getDoctor()
                        .getSpecialization()
        );

        response.setAppointmentDate(
                appointment
                        .getAppointmentDate()
        );

        response.setAppointmentTime(
                appointment
                        .getAppointmentTime()
        );

        response.setReason(
                appointment
                        .getReason()
        );

        response.setStatus(
                appointment
                        .getStatus()
        );

        return response;
    }
}