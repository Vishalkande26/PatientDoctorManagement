package com.patientmanagement.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.patientmanagement.entity.Patient;
import com.patientmanagement.exception.ResourceNotFoundException;
import com.patientmanagement.repository.AppointmentRepository;
import com.patientmanagement.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {

        patient = new Patient();

        patient.setId(1L);
        patient.setName("Rahul");
        patient.setAge(30);
        patient.setGender("Male");
        patient.setPhone("9876543210");
        patient.setDisease("Fever");
        patient.setAddress("Pune");
    }

    @Test
    void createPatient_ShouldReturnSavedPatient() {

        when(patientRepository.save(patient))
                .thenReturn(patient);

        Patient result =
                patientService.createPatient(patient);

        assertNotNull(result);
        assertEquals("Rahul", result.getName());
        assertEquals(30, result.getAge());

        verify(
                patientRepository,
                times(1)
        ).save(patient);
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() {

        when(
                patientRepository.findByIdAndDeletedFalse(1L)
        ).thenReturn(Optional.of(patient));

        Patient result =
                patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rahul", result.getName());

        verify(
                patientRepository,
                times(1)
        ).findByIdAndDeletedFalse(1L);
    }

    @Test
    void getPatientById_ShouldThrowException_WhenPatientDoesNotExist() {

        when(
                patientRepository.findByIdAndDeletedFalse(99L)
        ).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> patientService.getPatientById(99L)
        );

        verify(
                patientRepository,
                times(1)
        ).findByIdAndDeletedFalse(99L);
    }
}