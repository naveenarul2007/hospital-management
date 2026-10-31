package com.hospital.management.controller;

import com.hospital.management.model.Patient;
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
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listPatients(@RequestParam(required = false) String search, Model model) {
        List<Patient> patients;
        if (search != null && !search.isEmpty()) {
            patients = dataService.searchPatients(search);
            model.addAttribute("search", search);
        } else {
            patients = dataService.getAllPatients();
        }
        model.addAttribute("patients", patients);
        model.addAttribute("pageTitle", "Patient Management");
        addCommonAttributes(model);
        return "patients/list";
    }

    @GetMapping("/new")
    public String newPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        model.addAttribute("pageTitle", "Add New Patient");
        model.addAttribute("action", "add");
        addCommonAttributes(model);
        return "patients/form";
    }

    @PostMapping("/save")
    public String savePatient(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes) {
        try {
            dataService.savePatient(patient);
            redirectAttributes.addFlashAttribute("successMessage", "Patient saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving patient: " + e.getMessage());
        }
        return "redirect:/patients";
    }

    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findPatientById(id)
                .map(patient -> {
                    model.addAttribute("patient", patient);
                    model.addAttribute("pageTitle", "Edit Patient");
                    model.addAttribute("action", "edit");
                    addCommonAttributes(model);
                    return "patients/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Patient not found!");
                    return "redirect:/patients";
                });
    }

    @GetMapping("/view/{id}")
    public String viewPatient(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findPatientById(id)
                .map(patient -> {
                    model.addAttribute("patient", patient);
                    model.addAttribute("appointments", dataService.getAppointmentsByPatient(id));
                    model.addAttribute("medicalRecords", dataService.getRecordsByPatient(id));
                    model.addAttribute("labTests", dataService.getLabTestsByPatient(id));
                    model.addAttribute("bills", dataService.getBillsByPatient(id));
                    model.addAttribute("pageTitle", "Patient Details - " + patient.getFullName());
                    addCommonAttributes(model);
                    return "patients/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Patient not found!");
                    return "redirect:/patients";
                });
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deletePatient(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Patient deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Patient not found!");
        }
        return "redirect:/patients";
    }
}
