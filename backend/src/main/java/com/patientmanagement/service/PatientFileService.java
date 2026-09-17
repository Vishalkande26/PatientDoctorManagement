package com.patientmanagement.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.patientmanagement.dto.FileResponseDTO;
import com.patientmanagement.entity.PatientFile;
import com.patientmanagement.repository.PatientFileRepository;

@Service
public class PatientFileService {

    private final PatientFileRepository
            patientFileRepository;


    private static final long MAX_FILE_SIZE =
            10L * 1024L * 1024L;


    private static final List<String>
            ALLOWED_CONTENT_TYPES = List.of(

            "application/pdf",

            "image/jpeg",

            "image/png",

            "application/msword",

            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );


    public PatientFileService(
            PatientFileRepository patientFileRepository) {

        this.patientFileRepository =
                patientFileRepository;
    }


    // =====================================================
    // UPLOAD
    // =====================================================

    public FileResponseDTO uploadFile(
            Long patientId,
            MultipartFile file)
            throws Exception {

        validateFile(file);


        PatientFile patientFile =
                new PatientFile();


        patientFile.setPatientId(
                patientId
        );


        patientFile.setOriginalFileName(
                cleanFileName(
                        file.getOriginalFilename()
                )
        );


        patientFile.setContentType(
                file.getContentType()
        );


        patientFile.setFileSize(
                file.getSize()
        );


        patientFile.setFileData(
                file.getBytes()
        );


        LocalDateTime now =
                LocalDateTime.now();


        patientFile.setUploadedAt(
                now
        );


        patientFile.setUpdatedAt(
                now
        );


        PatientFile saved =
                patientFileRepository.save(
                        patientFile
                );


        return convertToDTO(saved);
    }


    // =====================================================
    // GET FILE METADATA BY PATIENT
    // =====================================================

    public List<FileResponseDTO>
    getFilesByPatient(
            Long patientId) {

        return patientFileRepository
                .findByPatientIdOrderByUploadedAtDesc(
                        patientId
                )
                .stream()
                .map(this::convertToDTO)
                .toList();
    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    public PatientFile
    getFileForDownload(
            Long id) {

        return patientFileRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "File not found with ID: "
                                        + id
                        )
                );
    }


    // =====================================================
    // REPLACE
    // =====================================================

    public FileResponseDTO
    replaceFile(
            Long id,
            MultipartFile file)
            throws Exception {

        validateFile(file);


        PatientFile existingFile =
                patientFileRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "File not found with ID: "
                                                + id
                                )
                        );


        existingFile.setOriginalFileName(
                cleanFileName(
                        file.getOriginalFilename()
                )
        );


        existingFile.setContentType(
                file.getContentType()
        );


        existingFile.setFileSize(
                file.getSize()
        );


        existingFile.setFileData(
                file.getBytes()
        );


        existingFile.setUpdatedAt(
                LocalDateTime.now()
        );


        PatientFile updated =
                patientFileRepository.save(
                        existingFile
                );


        return convertToDTO(updated);
    }


    // =====================================================
    // DELETE
    // =====================================================

    public void deleteFile(
            Long id) {

        if (!patientFileRepository
                .existsById(id)) {

            throw new RuntimeException(
                    "File not found with ID: "
                            + id
            );
        }


        patientFileRepository.deleteById(id);
    }


    // =====================================================
    // VALIDATION
    // =====================================================

    private void validateFile(
            MultipartFile file) {

        if (file == null) {

            throw new RuntimeException(
                    "File is required"
            );
        }


        if (file.isEmpty()) {

            throw new RuntimeException(
                    "File cannot be empty"
            );
        }


        if (file.getSize() > MAX_FILE_SIZE) {

            throw new RuntimeException(
                    "File size cannot exceed 10 MB"
            );
        }


        String contentType =
                file.getContentType();


        if (contentType == null ||
                !ALLOWED_CONTENT_TYPES
                        .contains(contentType)) {

            throw new RuntimeException(
                    "Invalid file type. Allowed types: PDF, JPG, PNG, DOC and DOCX"
            );
        }


        String fileName =
                file.getOriginalFilename();


        if (fileName == null ||
                !hasAllowedExtension(fileName)) {

            throw new RuntimeException(
                    "Invalid file extension. Allowed extensions: .pdf, .jpg, .jpeg, .png, .doc and .docx"
            );
        }
    }


    // =====================================================
    // EXTENSION VALIDATION
    // =====================================================

    private boolean hasAllowedExtension(
            String fileName) {

        String lowerName =
                fileName.toLowerCase();


        return lowerName.endsWith(".pdf")
                || lowerName.endsWith(".jpg")
                || lowerName.endsWith(".jpeg")
                || lowerName.endsWith(".png")
                || lowerName.endsWith(".doc")
                || lowerName.endsWith(".docx");
    }


    // =====================================================
    // CLEAN FILE NAME
    // =====================================================

    private String cleanFileName(
            String fileName) {

        if (fileName == null ||
                fileName.isBlank()) {

            return "unknown-file";
        }


        return fileName
                .replace("\\", "_")
                .replace("/", "_");
    }


    // =====================================================
    // ENTITY → DTO
    // =====================================================

    private FileResponseDTO convertToDTO(
            PatientFile file) {

        return new FileResponseDTO(

                file.getId(),

                file.getPatientId(),

                file.getOriginalFileName(),

                file.getContentType(),

                file.getFileSize(),

                file.getUploadedAt(),

                file.getUpdatedAt()
        );
    }
}