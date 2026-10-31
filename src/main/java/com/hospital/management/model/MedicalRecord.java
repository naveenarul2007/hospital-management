package com.hospital.management.model;

import java.time.LocalDateTime;

public class MedicalRecord {
    private String id;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String appointmentId;
    private String diagnosis;
    private String symptoms;
    private String observations;
    private String prescription;
    private String treatment;
    private String followUpDate;
    private LocalDateTime recordedAt;
    private String labTests;
    private String notes;

    public MedicalRecord() {}

    public MedicalRecord(String id, String patientId, String patientName, String doctorId,
                         String doctorName, String appointmentId, String diagnosis,
                         String symptoms, String observations, String prescription,
                         String treatment) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.observations = observations;
        this.prescription = prescription;
        this.treatment = treatment;
        this.recordedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getDoctorId() { return doctorId; }
    public void setDoctorId(String doctorId) { this.doctorId = doctorId; }
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public String getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(String followUpDate) { this.followUpDate = followUpDate; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    public String getLabTests() { return labTests; }
    public void setLabTests(String labTests) { this.labTests = labTests; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
