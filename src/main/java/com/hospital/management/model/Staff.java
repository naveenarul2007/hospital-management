package com.hospital.management.model;

import java.time.LocalDateTime;

public class Staff {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role; // NURSE, TECHNICIAN, RECEPTIONIST, ADMIN, PHARMACIST, LAB_TECHNICIAN
    private String department;
    private String qualification;
    private String shift; // MORNING, AFTERNOON, NIGHT
    private double salary;
    private String gender;
    private String status; // ACTIVE, ON_LEAVE, INACTIVE
    private LocalDateTime joinedAt;
    private String address;
    private String emergencyContact;

    public Staff() {}

    public Staff(String id, String firstName, String lastName, String email, String phone,
                 String role, String department, String qualification, String shift,
                 double salary, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.department = department;
        this.qualification = qualification;
        this.shift = shift;
        this.salary = salary;
        this.gender = gender;
        this.status = "ACTIVE";
        this.joinedAt = LocalDateTime.now();
    }

    public String getFullName() {
        return firstName + " " + lastName;
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
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
}
