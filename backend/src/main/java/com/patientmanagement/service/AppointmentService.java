package com.patientmanagement.service;

import java.util.List;

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


    

    public AppointmentResponseDTO createAppointment(
            AppointmentRequestDTO request) {

        boolean duplicate =
                appointmentRepository
                        .existsByDoctor_IdAndAppointmentDateAndAppointmentTime(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime()
                        );

        if (duplicate) {

            throw new ConflictException(
                    "Appointment already exists for this doctor, date and time."
            );
        }


        

        Patient patient =
                patientRepository
                        .findById(
                                request.getPatientId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + request.getPatientId()
                                )
                        );


        

        Doctor doctor =
                doctorRepository
                        .findById(
                                request.getDoctorId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: "
                                                + request.getDoctorId()
                                )
                        );


       
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


        return convertToResponse(
                savedAppointment
        );
    }


   

    public List<AppointmentResponseDTO>
    getAllAppointments() {

        return appointmentRepository
                .findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }


   

    public AppointmentResponseDTO
    getAppointmentById(
            Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );

        return convertToResponse(
                appointment
        );
    }


   

    public AppointmentResponseDTO
    updateAppointment(
            Long id,
            AppointmentRequestDTO request) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );


       

        boolean duplicate =
                appointmentRepository
                        .existsByDoctor_IdAndAppointmentDateAndAppointmentTimeAndIdNot(
                                request.getDoctorId(),
                                request.getAppointmentDate(),
                                request.getAppointmentTime(),
                                id
                        );

        if (duplicate) {

            throw new ConflictException(
                    "Appointment already exists for this doctor, date and time."
            );
        }


        

        Patient patient =
                patientRepository
                        .findById(
                                request.getPatientId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Patient not found with id: "
                                                + request.getPatientId()
                                )
                        );


        

        Doctor doctor =
                doctorRepository
                        .findById(
                                request.getDoctorId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: "
                                                + request.getDoctorId()
                                )
                        );


       

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


        return convertToResponse(
                updatedAppointment
        );
    }


    

    public void deleteAppointment(
            Long id) {

        Appointment appointment =
                appointmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Appointment not found with id: "
                                                + id
                                )
                        );

        appointmentRepository.delete(
                appointment
        );
    }


    

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