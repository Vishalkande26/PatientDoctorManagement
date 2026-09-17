package com.patientmanagement.controller;

import java.util.List;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.patientmanagement.dto.FileResponseDTO;
import com.patientmanagement.entity.PatientFile;
import com.patientmanagement.service.PatientFileService;

@RestController
@RequestMapping("/api/files")
public class PatientFileController {

    private final PatientFileService
            patientFileService;


    public PatientFileController(
            PatientFileService patientFileService) {

        this.patientFileService =
                patientFileService;
    }


    // =====================================================
    // UPLOAD
    // =====================================================

    @PostMapping(
            value = "/patient/{patientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileResponseDTO>
    uploadFile(

            @PathVariable Long patientId,

            @RequestParam("file")
            MultipartFile file)

            throws Exception {

        FileResponseDTO response =
                patientFileService.uploadFile(
                        patientId,
                        file
                );


        return ResponseEntity
                .ok(response);
    }


    // =====================================================
    // LIST FILE METADATA
    // =====================================================

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<FileResponseDTO>>
    getFilesByPatient(

            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                patientFileService
                        .getFilesByPatient(
                                patientId
                        )
        );
    }


    // =====================================================
    // DOWNLOAD
    // =====================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]>
    downloadFile(
            @PathVariable Long id) {

        PatientFile file =
                patientFileService
                        .getFileForDownload(id);


        MediaType mediaType;


        try {

            mediaType =
                    MediaType.parseMediaType(
                            file.getContentType()
                    );

        } catch (Exception e) {

            mediaType =
                    MediaType.APPLICATION_OCTET_STREAM;
        }


        HttpHeaders headers =
                new HttpHeaders();


        headers.setContentType(
                mediaType
        );


        headers.setContentDisposition(
                ContentDisposition
                        .attachment()
                        .filename(
                                file.getOriginalFileName()
                        )
                        .build()
        );


        headers.setContentLength(
                file.getFileSize()
        );


        return ResponseEntity
                .ok()
                .headers(headers)
                .body(
                        file.getFileData()
                );
    }


    // =====================================================
    // REPLACE
    // =====================================================

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileResponseDTO>
    replaceFile(

            @PathVariable Long id,

            @RequestParam("file")
            MultipartFile file)

            throws Exception {

        FileResponseDTO response =
                patientFileService.replaceFile(
                        id,
                        file
                );


        return ResponseEntity
                .ok(response);
    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String>
    deleteFile(
            @PathVariable Long id) {

        patientFileService.deleteFile(id);


        return ResponseEntity.ok(
                "File deleted successfully"
        );
    }
}