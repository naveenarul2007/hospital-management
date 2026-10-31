package com.hospital.management.controller;

import com.hospital.management.model.Doctor;
import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listDoctors(@RequestParam(required = false) String search,
                               @RequestParam(required = false) String department,
                               Model model) {
        List<Doctor> doctors;
        if (search != null && !search.isEmpty()) {
            doctors = dataService.searchDoctors(search);
            model.addAttribute("search", search);
        } else if (department != null && !department.isEmpty()) {
            doctors = dataService.getDoctorsByDepartment(department);
            model.addAttribute("selectedDepartment", department);
        } else {
            doctors = dataService.getAllDoctors();
        }
        model.addAttribute("doctors", doctors);
        model.addAttribute("departments", dataService.getDepartments());
        model.addAttribute("pageTitle", "Doctor Management");
        addCommonAttributes(model);
        return "doctors/list";
    }

    @GetMapping("/new")
    public String newDoctorForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        model.addAttribute("departments", dataService.getDepartments());
        model.addAttribute("pageTitle", "Add New Doctor");
        model.addAttribute("action", "add");
        addCommonAttributes(model);
        return "doctors/form";
    }

    @PostMapping("/save")
    public String saveDoctor(@ModelAttribute Doctor doctor, RedirectAttributes redirectAttributes) {
        try {
            dataService.saveDoctor(doctor);
            redirectAttributes.addFlashAttribute("successMessage", "Doctor saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving doctor: " + e.getMessage());
        }
        return "redirect:/doctors";
    }

    @GetMapping("/edit/{id}")
    public String editDoctorForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findDoctorById(id)
                .map(doctor -> {
                    model.addAttribute("doctor", doctor);
                    model.addAttribute("departments", dataService.getDepartments());
                    model.addAttribute("pageTitle", "Edit Doctor");
                    model.addAttribute("action", "edit");
                    addCommonAttributes(model);
                    return "doctors/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Doctor not found!");
                    return "redirect:/doctors";
                });
    }

    @GetMapping("/view/{id}")
    public String viewDoctor(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findDoctorById(id)
                .map(doctor -> {
                    model.addAttribute("doctor", doctor);
                    model.addAttribute("appointments", dataService.getAppointmentsByDoctor(id));
                    model.addAttribute("pageTitle", "Doctor Details - " + doctor.getFullName());
                    addCommonAttributes(model);
                    return "doctors/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Doctor not found!");
                    return "redirect:/doctors";
                });
    }

    @PostMapping("/delete/{id}")
    public String deleteDoctor(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteDoctor(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Doctor deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Doctor not found!");
        }
        return "redirect:/doctors";
    }
}
