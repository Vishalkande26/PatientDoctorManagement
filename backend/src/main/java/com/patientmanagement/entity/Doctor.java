package com.patientmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Size(
        min = 2,
        max = 100,
        message = "Doctor name must be between 2 and 100 characters"
    )
    private String name;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotBlank(message = "Phone is required")
    @Pattern(
        regexp = "^[0-9]{10}$",
        message = "Phone number must be exactly 10 digits"
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotNull(message = "Experience is required")
    @Min(
        value = 0,
        message = "Experience cannot be negative"
    )
    private Integer experience;


    public Doctor() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(
            String specialization) {

        this.specialization =
                specialization;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Integer getExperience() {
        return experience;
    }

    public void setExperience(
            Integer experience) {

        this.experience =
                experience;
    }
}