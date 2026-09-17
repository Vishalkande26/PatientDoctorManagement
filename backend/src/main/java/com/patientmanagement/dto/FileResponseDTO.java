package com.patientmanagement.dto;

import java.time.LocalDateTime;

public class FileResponseDTO {

    private Long id;

    private Long patientId;

    private String originalFileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    private LocalDateTime updatedAt;


    public FileResponseDTO() {
    }


    public FileResponseDTO(
            Long id,
            Long patientId,
            String originalFileName,
            String contentType,
            Long fileSize,
            LocalDateTime uploadedAt,
            LocalDateTime updatedAt) {

        this.id = id;
        this.patientId = patientId;
        this.originalFileName =
                originalFileName;
        this.contentType =
                contentType;
        this.fileSize =
                fileSize;
        this.uploadedAt =
                uploadedAt;
        this.updatedAt =
                updatedAt;
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


    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(
            LocalDateTime uploadedAt) {

        this.uploadedAt =
                uploadedAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(
            LocalDateTime updatedAt) {

        this.updatedAt =
                updatedAt;
    }
}