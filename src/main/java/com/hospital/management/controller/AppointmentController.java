package com.hospital.management.controller;

import com.hospital.management.model.Appointment;
import com.hospital.management.model.Doctor;
import com.hospital.management.model.Patient;
import com.hospital.management.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listAppointments(@RequestParam(required = false) String status,
                                    @RequestParam(required = false) String date,
                                    Model model) {
        List<Appointment> appointments;
        if (status != null && !status.isEmpty()) {
            appointments = dataService.getAllAppointments().stream()
                    .filter(a -> a.getStatus().equals(status)).toList();
            model.addAttribute("filterStatus", status);
        } else if ("today".equals(date)) {
            appointments = dataService.getTodayAppointments();
            model.addAttribute("filterDate", "today");
        } else {
            appointments = dataService.getAllAppointments();
        }
        model.addAttribute("appointments", appointments);
        model.addAttribute("pageTitle", "Appointment Management");
        addCommonAttributes(model);
        return "appointments/list";
    }

    @GetMapping("/new")
    public String newAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", dataService.getAllPatients());
        model.addAttribute("doctors", dataService.getAllDoctors());
        model.addAttribute("departments", dataService.getDepartments());
        model.addAttribute("pageTitle", "Book Appointment");
        model.addAttribute("action", "add");
        addCommonAttributes(model);
        return "appointments/form";
    }

    @PostMapping("/save")
    public String saveAppointment(@RequestParam String patientId,
                                   @RequestParam String doctorId,
                                   @RequestParam String department,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
                                   @RequestParam String appointmentTime,
                                   @RequestParam(required = false) String reason,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String appointmentId,
                                   RedirectAttributes redirectAttributes) {
        try {
            LocalTime time = LocalTime.parse(appointmentTime);

            // Check if slot is available (for new appointments)
            if (appointmentId == null || appointmentId.isEmpty()) {
                if (!dataService.isSlotAvailable(doctorId, appointmentDate, time)) {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "This time slot is already booked. Please choose another time.");
                    return "redirect:/appointments/new";
                }
            }

            Patient patient = dataService.findPatientById(patientId).orElseThrow();
            Doctor doctor = dataService.findDoctorById(doctorId).orElseThrow();

            Appointment appointment = new Appointment();
            if (appointmentId != null && !appointmentId.isEmpty()) {
                appointment.setId(appointmentId);
            }
            appointment.setPatientId(patientId);
            appointment.setPatientName(patient.getFullName());
            appointment.setDoctorId(doctorId);
            appointment.setDoctorName(doctor.getFullName());
            appointment.setDepartment(department);
            appointment.setAppointmentDate(appointmentDate);
            appointment.setAppointmentTime(time);
            appointment.setReason(reason);
            appointment.setConsultationFee(doctor.getConsultationFee());
            appointment.setType(type != null ? type : "IN_PERSON");

            dataService.saveAppointment(appointment);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment booked successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error booking appointment: " + e.getMessage());
        }
        return "redirect:/appointments";
    }

    @GetMapping("/edit/{id}")
    public String editAppointment(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findAppointmentById(id)
                .map(appointment -> {
                    model.addAttribute("appointment", appointment);
                    model.addAttribute("patients", dataService.getAllPatients());
                    model.addAttribute("doctors", dataService.getAllDoctors());
                    model.addAttribute("departments", dataService.getDepartments());
                    model.addAttribute("pageTitle", "Edit Appointment");
                    model.addAttribute("action", "edit");
                    addCommonAttributes(model);
                    return "appointments/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Appointment not found!");
                    return "redirect:/appointments";
                });
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable String id,
                                @RequestParam String status,
                                @RequestParam(required = false) String notes,
                                RedirectAttributes redirectAttributes) {
        dataService.findAppointmentById(id).ifPresent(appointment -> {
            appointment.setStatus(status);
            if (notes != null && !notes.isEmpty()) appointment.setNotes(notes);
            dataService.saveAppointment(appointment);

            String notifMsg = switch (status) {
                case "CONFIRMED" -> "Appointment " + id + " has been confirmed.";
                case "CANCELLED" -> "Appointment " + id + " has been cancelled.";
                case "COMPLETED" -> "Appointment " + id + " has been completed.";
                default -> "Appointment " + id + " status updated to " + status;
            };
            dataService.addNotification("Appointment Update", notifMsg,
                    "CONFIRMED".equals(status) ? "SUCCESS" : "INFO", "ALL");
        });
        redirectAttributes.addFlashAttribute("successMessage", "Appointment status updated!");
        return "redirect:/appointments";
    }

    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteAppointment(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Appointment not found!");
        }
        return "redirect:/appointments";
    }
}
