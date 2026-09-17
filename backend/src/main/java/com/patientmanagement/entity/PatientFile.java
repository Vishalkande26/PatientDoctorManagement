package com.patientmanagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient_files")
public class PatientFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "patient_id",
        nullable = false
    )
    private Long patientId;

    @Column(
        name = "original_file_name",
        nullable = false,
        length = 255
    )
    private String originalFileName;

    @Column(
        name = "content_type",
        nullable = false,
        length = 150
    )
    private String contentType;

    @Column(
        name = "file_size",
        nullable = false
    )
    private Long fileSize;

    @Column(
        name = "file_data",
        nullable = false,
        columnDefinition = "varbinary(max)"
    )
    private byte[] fileData;

    @Column(
        name = "uploaded_at",
        nullable = false
    )
    private LocalDateTime uploadedAt;

    @Column(
        name = "updated_at",
        nullable = false
    )
    private LocalDateTime updatedAt;


    public PatientFile() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }


    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(
            String originalFileName) {

        this.originalFileName =
                originalFileName;
    }


    public String getContentType() {
        return contentType;
    }

    public void setContentType(
            String contentType) {

        this.contentType =
                contentType;
    }


    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(
            byte[] fileData) {

        this.fileData = fileData;
    }


    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(
            LocalDateTime uploadedAt) {

        this.uploadedAt = uploadedAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt) {

        this.updatedAt = updatedAt;
    }
}