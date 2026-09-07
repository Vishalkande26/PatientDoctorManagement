
package com.patientmanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.patientmanagement.entity.Doctor;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;


    public DoctorService(
            DoctorRepository doctorRepository) {

        this.doctorRepository =
                doctorRepository;
    }


    

    public Doctor createDoctor(
            Doctor doctor) {

        return doctorRepository.save(
                doctor
        );
    }


    

    public List<Doctor> getAllDoctors() {

        return doctorRepository.findAll();
    }


    

    public Doctor getDoctorById(
            Long id) {

        return doctorRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Doctor not found with id: "
                                        + id
                        )
                );
    }


    

    public Doctor updateDoctor(
            Long id,
            Doctor doctor) {

        Doctor existingDoctor =
                doctorRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: "
                                                + id
                                )
                        );


        existingDoctor.setName(
                doctor.getName()
        );


        existingDoctor.setSpecialization(
                doctor.getSpecialization()
        );


        existingDoctor.setPhone(
                doctor.getPhone()
        );


        existingDoctor.setEmail(
                doctor.getEmail()
        );


        existingDoctor.setExperience(
                doctor.getExperience()
        );


        return doctorRepository.save(
                existingDoctor
        );
    }


    

    public void deleteDoctor(
            Long id) {

        Doctor existingDoctor =
                doctorRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Doctor not found with id: "
                                                + id
                                )
                        );


        doctorRepository.delete(
                existingDoctor
        );
    }
}