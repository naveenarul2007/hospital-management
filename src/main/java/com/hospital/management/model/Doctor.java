package com.hospital.management.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Doctor {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private String qualification;
    private String department;
    private double consultationFee;
    private String experience; // years
    private String availableDays; // Mon-Fri, etc.
    private String availableTime; // 09:00 - 17:00
    private String gender;
    private String status; // ACTIVE, ON_LEAVE, INACTIVE
    private LocalDateTime joinedAt;
    private String licenseNumber;
    private String bio;

    public Doctor() {}

    public Doctor(String id, String firstName, String lastName, String email, String phone,
                  String specialization, String qualification, String department,
                  double consultationFee, String experience, String availableDays,
                  String availableTime, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
        this.qualification = qualification;
        this.department = department;
        this.consultationFee = consultationFee;
        this.experience = experience;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
        this.gender = gender;
        this.status = "ACTIVE";
        this.joinedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return "Dr. " + firstName + " " + lastName;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }
    public String getAvailableTime() { return availableTime; }
    public void setAvailableTime(String availableTime) { this.availableTime = availableTime; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
