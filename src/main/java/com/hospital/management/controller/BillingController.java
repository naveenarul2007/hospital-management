package com.hospital.management.controller;

import com.hospital.management.model.Bill;
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
@RequestMapping("/billing")
public class BillingController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listBills(@RequestParam(required = false) String status, Model model) {
        List<Bill> bills;
        if (status != null && !status.isEmpty()) {
            bills = dataService.getAllBills().stream()
                    .filter(b -> b.getPaymentStatus().equals(status)).toList();
            model.addAttribute("filterStatus", status);
        } else {
            bills = dataService.getAllBills();
        }
        model.addAttribute("bills", bills);
        model.addAttribute("totalRevenue", dataService.getTotalRevenue());
        model.addAttribute("pageTitle", "Billing Management");
        addCommonAttributes(model);
        return "billing/list";
    }

    @GetMapping("/new")
    public String newBillForm(@RequestParam(required = false) String patientId,
                               @RequestParam(required = false) String appointmentId,
                               Model model) {
        Bill bill = new Bill();
        if (patientId != null) {
            bill.setPatientId(patientId);
            dataService.findPatientById(patientId)
                    .ifPresent(p -> bill.setPatientName(p.getFullName()));
        }
        if (appointmentId != null) {
            bill.setAppointmentId(appointmentId);
            dataService.findAppointmentById(appointmentId)
                    .ifPresent(a -> bill.setConsultationCharge(a.getConsultationFee()));
        }
        model.addAttribute("bill", bill);
        model.addAttribute("patients", dataService.getAllPatients());
        model.addAttribute("appointments", dataService.getAllAppointments());
        model.addAttribute("pageTitle", "Generate Bill");
        addCommonAttributes(model);
        return "billing/form";
    }

    @PostMapping("/save")
    public String saveBill(@RequestParam String patientId,
                            @RequestParam(required = false) String appointmentId,
                            @RequestParam double consultationCharge,
                            @RequestParam double labCharge,
                            @RequestParam double medicineCharge,
                            @RequestParam double otherCharges,
                            @RequestParam double discountAmount,
                            @RequestParam(required = false) String notes,
                            @RequestParam(required = false) String billId,
                            RedirectAttributes redirectAttributes) {
        try {
            var patient = dataService.findPatientById(patientId).orElseThrow();

            Bill bill = new Bill();
            if (billId != null && !billId.isEmpty()) bill.setId(billId);
            bill.setPatientId(patientId);
            bill.setPatientName(patient.getFullName());
            bill.setAppointmentId(appointmentId);
            bill.setConsultationCharge(consultationCharge);
            bill.setLabCharge(labCharge);
            bill.setMedicineCharge(medicineCharge);
            bill.setOtherCharges(otherCharges);
            bill.setDiscountAmount(discountAmount);
            bill.setNotes(notes);
            bill.calculateTotal();

            dataService.saveBill(bill);
            redirectAttributes.addFlashAttribute("successMessage", "Bill generated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/billing";
    }

    @GetMapping("/view/{id}")
    public String viewBill(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findBillById(id)
                .map(bill -> {
                    model.addAttribute("bill", bill);
                    model.addAttribute("pageTitle", "Invoice - " + id);
                    addCommonAttributes(model);
                    return "billing/view";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Bill not found!");
                    return "redirect:/billing";
                });
    }

    @PostMapping("/pay/{id}")
    public String processPayment(@PathVariable String id,
                                  @RequestParam double paidAmount,
                                  @RequestParam String paymentMethod,
                                  RedirectAttributes redirectAttributes) {
        dataService.findBillById(id).ifPresent(bill -> {
            bill.setPaidAmount(paidAmount);
            bill.setPaymentMethod(paymentMethod);
            if (paidAmount >= bill.getTotalAmount()) {
                bill.setPaymentStatus("PAID");
                bill.setPaidAt(LocalDateTime.now());
                dataService.addNotification("Payment Received",
                        "Payment of ₹" + paidAmount + " received from " + bill.getPatientName(),
                        "SUCCESS", "ADMIN");
            } else if (paidAmount > 0) {
                bill.setPaymentStatus("PARTIAL");
            }
            dataService.saveBill(bill);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Payment processed successfully!");
        return "redirect:/billing";
    }

    @PostMapping("/delete/{id}")
    public String deleteBill(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteBill(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Bill deleted!");
        }
        return "redirect:/billing";
    }
}
