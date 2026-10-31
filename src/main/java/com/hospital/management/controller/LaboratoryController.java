package com.hospital.management.controller;

import com.hospital.management.model.LabTest;
import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/laboratory")
public class LaboratoryController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listTests(@RequestParam(required = false) String status, Model model) {
        List<LabTest> tests;
        if (status != null && !status.isEmpty()) {
            tests = dataService.getAllLabTests().stream()
                    .filter(t -> t.getStatus().equals(status)).toList();
            model.addAttribute("filterStatus", status);
        } else {
            tests = dataService.getAllLabTests();
        }
        model.addAttribute("tests", tests);
        model.addAttribute("pageTitle", "Laboratory Management");
        addCommonAttributes(model);
        return "laboratory/list";
    }

    @GetMapping("/new")
    public String newTestForm(Model model) {
        model.addAttribute("test", new LabTest());
        model.addAttribute("patients", dataService.getAllPatients());
        model.addAttribute("doctors", dataService.getAllDoctors());
        model.addAttribute("pageTitle", "Add Lab Test");
        addCommonAttributes(model);
        return "laboratory/form";
    }

    @PostMapping("/save")
    public String saveTest(@RequestParam String patientId,
                            @RequestParam String doctorId,
                            @RequestParam String testName,
                            @RequestParam String testType,
                            @RequestParam double price,
                            @RequestParam(required = false) String appointmentId,
                            @RequestParam(required = false) String testId,
                            RedirectAttributes redirectAttributes) {
        try {
            var patient = dataService.findPatientById(patientId).orElseThrow();
            var doctor = dataService.findDoctorById(doctorId).orElseThrow();

            LabTest test = new LabTest();
            if (testId != null && !testId.isEmpty()) test.setId(testId);
            test.setPatientId(patientId);
            test.setPatientName(patient.getFullName());
            test.setDoctorId(doctorId);
            test.setDoctorName(doctor.getFullName());
            test.setTestName(testName);
            test.setTestType(testType);
            test.setPrice(price);
            if (appointmentId != null) test.setAppointmentId(appointmentId);

            dataService.saveLabTest(test);
            redirectAttributes.addFlashAttribute("successMessage", "Lab test created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/laboratory";
    }

    @PostMapping("/result/{id}")
    public String enterResult(@PathVariable String id,
                               @RequestParam String result,
                               @RequestParam(required = false) String normalRange,
                               @RequestParam(required = false) String unit,
                               @RequestParam(required = false) String remarks,
                               @RequestParam String status,
                               RedirectAttributes redirectAttributes) {
        dataService.findLabTestById(id).ifPresent(test -> {
            test.setResult(result);
            test.setNormalRange(normalRange);
            test.setUnit(unit);
            test.setRemarks(remarks);
            test.setStatus(status);
            if ("COMPLETED".equals(status)) {
                test.setCompletedAt(LocalDateTime.now());
                dataService.addNotification("Lab Result Ready",
                        "Lab result for " + test.getPatientName() + " (" + test.getTestName() + ") is ready.",
                        "SUCCESS", "ALL");
            }
            dataService.saveLabTest(test);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Test result updated!");
        return "redirect:/laboratory";
    }

    @PostMapping("/delete/{id}")
    public String deleteTest(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteLabTest(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Lab test deleted!");
        }
        return "redirect:/laboratory";
    }
}
