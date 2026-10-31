package com.hospital.management.controller;

import com.hospital.management.model.Staff;
import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("staffList", dataService.getAllStaff());
        model.addAttribute("departments", dataService.getDepartments());
        model.addAttribute("pageTitle", "Staff Management");
        addCommonAttributes(model);
        return "staff/list";
    }

    @GetMapping("/new")
    public String newStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        model.addAttribute("departments", dataService.getDepartments());
        model.addAttribute("pageTitle", "Add Staff Member");
        addCommonAttributes(model);
        return "staff/form";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute Staff staff, RedirectAttributes redirectAttributes) {
        try {
            dataService.saveStaff(staff);
            redirectAttributes.addFlashAttribute("successMessage", "Staff member saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/staff";
    }

    @GetMapping("/edit/{id}")
    public String editStaff(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findStaffById(id)
                .map(staff -> {
                    model.addAttribute("staff", staff);
                    model.addAttribute("departments", dataService.getDepartments());
                    model.addAttribute("pageTitle", "Edit Staff");
                    addCommonAttributes(model);
                    return "staff/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Staff not found!");
                    return "redirect:/staff";
                });
    }

    @PostMapping("/delete/{id}")
    public String deleteStaff(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteStaff(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Staff deleted!");
        }
        return "redirect:/staff";
    }
}
