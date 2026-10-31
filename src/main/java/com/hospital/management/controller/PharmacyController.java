package com.hospital.management.controller;

import com.hospital.management.model.Medicine;
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
@RequestMapping("/pharmacy")
public class PharmacyController {

    @Autowired
    private DataService dataService;

    private void addCommonAttributes(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("userRole", auth.getAuthorities().iterator().next().getAuthority());
        model.addAttribute("unreadCount", dataService.getUnreadNotifications().size());
    }

    @GetMapping
    public String listMedicines(@RequestParam(required = false) String search,
                                 @RequestParam(required = false) String status,
                                 Model model) {
        List<Medicine> medicines;
        if ("low_stock".equals(status)) {
            medicines = dataService.getLowStockMedicines();
            model.addAttribute("filterStatus", "low_stock");
        } else {
            medicines = dataService.getAllMedicines();
        }
        if (search != null && !search.isEmpty()) {
            medicines = medicines.stream()
                    .filter(m -> m.getName().toLowerCase().contains(search.toLowerCase()) ||
                            m.getGenericName().toLowerCase().contains(search.toLowerCase()))
                    .toList();
            model.addAttribute("search", search);
        }
        model.addAttribute("medicines", medicines);
        model.addAttribute("lowStockCount", dataService.getLowStockMedicines().size());
        model.addAttribute("pageTitle", "Pharmacy Management");
        addCommonAttributes(model);
        return "pharmacy/list";
    }

    @GetMapping("/new")
    public String newMedicineForm(Model model) {
        model.addAttribute("medicine", new Medicine());
        model.addAttribute("pageTitle", "Add Medicine");
        addCommonAttributes(model);
        return "pharmacy/form";
    }

    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute Medicine medicine, RedirectAttributes redirectAttributes) {
        try {
            medicine.updateStatus();
            dataService.saveMedicine(medicine);
            redirectAttributes.addFlashAttribute("successMessage", "Medicine saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/pharmacy";
    }

    @GetMapping("/edit/{id}")
    public String editMedicine(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        return dataService.findMedicineById(id)
                .map(medicine -> {
                    model.addAttribute("medicine", medicine);
                    model.addAttribute("pageTitle", "Edit Medicine");
                    addCommonAttributes(model);
                    return "pharmacy/form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "Medicine not found!");
                    return "redirect:/pharmacy";
                });
    }

    @PostMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable String id, RedirectAttributes redirectAttributes) {
        if (dataService.deleteMedicine(id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Medicine deleted!");
        }
        return "redirect:/pharmacy";
    }

    @PostMapping("/restock/{id}")
    public String restockMedicine(@PathVariable String id,
                                   @RequestParam int quantity,
                                   RedirectAttributes redirectAttributes) {
        dataService.findMedicineById(id).ifPresent(medicine -> {
            medicine.setQuantity(medicine.getQuantity() + quantity);
            medicine.updateStatus();
            dataService.saveMedicine(medicine);
        });
        redirectAttributes.addFlashAttribute("successMessage", "Stock updated successfully!");
        return "redirect:/pharmacy";
    }
}
