--liquibase formatted sql

--changeset vishal:005-add-patient-soft-delete

ALTER TABLE patients
ADD deleted BIT NOT NULL
    CONSTRAINT DF_patients_deleted DEFAULT 0;