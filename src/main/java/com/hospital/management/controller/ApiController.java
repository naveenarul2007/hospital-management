package com.hospital.management.controller;

import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private DataService dataService;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return dataService.getDashboardStats();
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getNotifications() {
        return ResponseEntity.ok(Map.of(
                "notifications", dataService.getAllNotifications(),
                "unreadCount", dataService.getUnreadNotifications().size()
        ));
    }

    @PostMapping("/notifications/{id}/read")
    public ResponseEntity<?> markRead(@PathVariable String id) {
        dataService.markNotificationRead(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/notifications/read-all")
    public ResponseEntity<?> markAllRead() {
        dataService.markAllNotificationsRead();
        return ResponseEntity.ok(Map.of("success", true));
    }

    @GetMapping("/doctors/by-department/{dept}")
    public ResponseEntity<?> getDoctorsByDept(@PathVariable String dept) {
        var doctors = dataService.getDoctorsByDepartment(dept);
        return ResponseEntity.ok(doctors.stream().map(d -> Map.of(
                "id", d.getId(),
                "name", d.getFullName(),
                "specialization", d.getSpecialization(),
                "fee", d.getConsultationFee()
        )).toList());
    }

    @GetMapping("/patients/search")
    public ResponseEntity<?> searchPatients(@RequestParam String q) {
        return ResponseEntity.ok(dataService.searchPatients(q).stream().map(p -> Map.of(
                "id", p.getId(),
                "name", p.getFullName(),
                "phone", p.getPhone()
        )).toList());
    }

    @GetMapping("/check-slot")
    public ResponseEntity<?> checkSlot(@RequestParam String doctorId,
                                         @RequestParam String date,
                                         @RequestParam String time) {
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            java.time.LocalTime t = java.time.LocalTime.parse(time);
            boolean available = dataService.isSlotAvailable(doctorId, d, t);
            return ResponseEntity.ok(Map.of("available", available));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
