package com.hospital.management.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Medicine {
    private String id;
    private String name;
    private String genericName;
    private String category; // Tablet, Syrup, Injection, Capsule, etc.
    private String manufacturer;
    private int quantity;
    private int minimumStock;
    private double price;
    private LocalDate expiryDate;
    private String description;
    private LocalDateTime addedAt;
    private String status; // AVAILABLE, LOW_STOCK, OUT_OF_STOCK, EXPIRED

    public Medicine() {}

    public Medicine(String id, String name, String genericName, String category,
                    String manufacturer, int quantity, int minimumStock,
                    double price, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.genericName = genericName;
        this.category = category;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.minimumStock = minimumStock;
        this.price = price;
        this.expiryDate = expiryDate;
        this.addedAt = LocalDateTime.now();
        updateStatus();
    }

    public void updateStatus() {
        if (quantity == 0) {
            this.status = "OUT_OF_STOCK";
        } else if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            this.status = "EXPIRED";
        } else if (quantity <= minimumStock) {
            this.status = "LOW_STOCK";
        } else {
            this.status = "AVAILABLE";
        }
    }

    public boolean isLowStock() {
        return quantity <= minimumStock;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; updateStatus(); }
    public int getMinimumStock() { return minimumStock; }
    public void setMinimumStock(int minimumStock) { this.minimumStock = minimumStock; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
