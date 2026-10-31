package com.hospital.management.controller;

import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private DataService dataService;

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                        @RequestParam(required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> stats = dataService.getDashboardStats();
        model.addAttribute("stats", stats);
        model.addAttribute("recentAppointments", dataService.getTodayAppointments());
        model.addAttribute("notifications", dataService.getUnreadNotifications());
        model.addAttribute("lowStockMedicines", dataService.getLowStockMedicines());
        model.addAttribute("pageTitle", "Dashboard");
        addCommonAttributes(model);
        return "dashboard";
    }

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }
}
