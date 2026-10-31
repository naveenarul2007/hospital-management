package com.hospital.management.controller;

import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String reports(Model model) {
        // Appointment stats by status
        var appointments = dataService.getAllAppointments();
        Map<String, Long> appointmentsByStatus = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus(), Collectors.counting()));

        // Department stats
        Map<String, Long> appointmentsByDept = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getDepartment(), Collectors.counting()));

        // Revenue
        double totalRevenue = dataService.getTotalRevenue();
        double pendingRevenue = dataService.getAllBills().stream()
                .filter(b -> !"PAID".equals(b.getPaymentStatus()))
                .mapToDouble(b -> b.getTotalAmount() - b.getPaidAmount())
                .sum();

        // Medicine stats
        long lowStockCount = dataService.getLowStockMedicines().size();
        long totalMedicines = dataService.getAllMedicines().size();

        // Lab stats
        Map<String, Long> labByStatus = dataService.getAllLabTests().stream()
                .collect(Collectors.groupingBy(t -> t.getStatus(), Collectors.counting()));

        model.addAttribute("stats", dataService.getDashboardStats());
        model.addAttribute("appointmentsByStatus", appointmentsByStatus);
        model.addAttribute("appointmentsByDept", appointmentsByDept);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("pendingRevenue", pendingRevenue);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("totalMedicines", totalMedicines);
        model.addAttribute("labByStatus", labByStatus);
        model.addAttribute("pageTitle", "Reports & Analytics");
        addCommonAttributes(model);
        return "reports/index";
    }

    @GetMapping("/api/chart-data")
    @ResponseBody
    public Map<String, Object> getChartData() {
        Map<String, Object> data = new HashMap<>();

        // Appointments by status
        var appointments = dataService.getAllAppointments();
        Map<String, Long> byStatus = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getStatus(), Collectors.counting()));
        data.put("appointmentsByStatus", byStatus);

        // Appointments by department
        Map<String, Long> byDept = appointments.stream()
                .collect(Collectors.groupingBy(a -> a.getDepartment(), Collectors.counting()));
        data.put("appointmentsByDept", byDept);

        // Revenue data
        data.put("totalRevenue", dataService.getTotalRevenue());
        data.put("stats", dataService.getDashboardStats());

        // Medicine stock stats
        var medicines = dataService.getAllMedicines();
        long available = medicines.stream().filter(m -> "AVAILABLE".equals(m.getStatus())).count();
        long lowStock = medicines.stream().filter(m -> "LOW_STOCK".equals(m.getStatus())).count();
        long outOfStock = medicines.stream().filter(m -> "OUT_OF_STOCK".equals(m.getStatus())).count();
        data.put("medicineStats", Map.of("available", available, "lowStock", lowStock, "outOfStock", outOfStock));

        return data;
    }
}
