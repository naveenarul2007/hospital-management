package com.hospital.management.controller;

import com.hospital.management.model.MedicalRecord;
import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/medical-records")
public class MedicalRecordController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listRecords(Model model) {
        model.addAttribute("records", dataService.getAllMedicalRecords());
        model.addAttribute("pageTitle", "Medical Records");
        addCommonAttributes(model);
        return "medical-records/list";
    }

    @GetMapping("/new")
    public String newRecordForm(@RequestParam(required = false) String patientId,
                                 @RequestParam(required = false) String appointmentId,
                                 Model model) {
        MedicalRecord record = new MedicalRecord();
        if (patientId != null) {
            record.setPatientId(patientId);
            dataService.findPatientById(patientId).ifPresent(p -> record.setPatientName(p.getFullName()));
        }
        if (appointmentId != null) {
            record.setAppointmentId(appointmentId);
            dataService.findAppointmentById(appointmentId).ifPresent(a -> {
                record.setDoctorId(a.getDoctorId());
                record.setDoctorName(a.getDoctorName());
            });
        }
        model.addAttribute("record", record);
        model.addAttribute("patients", dataService.getAllPatients());
        model.addAttribute("doctors", dataService.getAllDoctors());
        model.addAttribute("pageTitle", "Add Medical Record");
        addCommonAttributes(model);
        return "medical-records/form";
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute MedicalRecord record, RedirectAttributes redirectAttributes) {
        try {
            dataService.saveMedicalRecord(record);
            redirectAttributes.addFlashAttribute("successMessage", "Medical record saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/medical-records";
    }

    @GetMapping("/view/{id}")
    public String viewRecord(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findRecordById(id)
                .map(record -> {
                    model.addAttribute("record", record);
                    model.addAttribute("pageTitle", "Medical Record Details");
                    addCommonAttributes(model);
                    return "medical-records/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Record not found!");
                    return "redirect:/medical-records";
                });
    }

    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findRecordById(id)
                .map(record -> {
                    model.addAttribute("record", record);
                    model.addAttribute("patients", dataService.getAllPatients());
                    model.addAttribute("doctors", dataService.getAllDoctors());
                    model.addAttribute("pageTitle", "Edit Medical Record");
                    addCommonAttributes(model);
                    return "medical-records/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Record not found!");
                    return "redirect:/medical-records";
                });
    }

    @PostMapping("/delete/{id}")
    public String deleteRecord(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteMedicalRecord(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Record deleted!");
        }
        return "redirect:/medical-records";
    }
}
