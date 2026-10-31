package com.hospital.management.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bill {
    private String id;
    private String patientId;
    private String patientName;
    private String appointmentId;
    private double consultationCharge;
    private double labCharge;
    private double medicineCharge;
    private double otherCharges;
    private double totalAmount;
    private double paidAmount;
    private double discountAmount;
    private String paymentStatus; // PENDING, PARTIAL, PAID, REFUNDED
    private String paymentMethod; // CASH, CARD, UPI, INSURANCE
    private LocalDateTime generatedAt;
    private LocalDateTime paidAt;
    private String notes;
    private List<BillItem> items;

    public Bill() {
        this.items = new ArrayList<>();
    }

    public Bill(String id, String patientId, String patientName, String appointmentId) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.paymentStatus = "PENDING";
        this.generatedAt = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public void calculateTotal() {
        this.totalAmount = consultationCharge + labCharge + medicineCharge + otherCharges - discountAmount;
    }

    public double getBalance() {
        return totalAmount - paidAmount;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    public String getAppointmentId() { return appointmentId; }
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }
    public double getConsultationCharge() { return consultationCharge; }
    public void setConsultationCharge(double consultationCharge) { this.consultationCharge = consultationCharge; }
    public double getLabCharge() { return labCharge; }
    public void setLabCharge(double labCharge) { this.labCharge = labCharge; }
    public double getMedicineCharge() { return medicineCharge; }
    public void setMedicineCharge(double medicineCharge) { this.medicineCharge = medicineCharge; }
    public double getOtherCharges() { return otherCharges; }
    public void setOtherCharges(double otherCharges) { this.otherCharges = otherCharges; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(double paidAmount) { this.paidAmount = paidAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public void setPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<BillItem> getItems() { return items; }
    public void setItems(List<BillItem> items) { this.items = items; }

    // Inner class for bill line items
    public static class BillItem {
        private String description;
        private int quantity;
        private double unitPrice;
        private double total;

        public BillItem() {}

        public BillItem(String description, int quantity, double unitPrice) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.total = quantity * unitPrice;
        }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
    }
}
