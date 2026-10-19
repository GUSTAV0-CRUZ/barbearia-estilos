package com.barbearia.estilos.appointments.controllers;

import com.barbearia.estilos.appointments.dtos.GenerateSlotsDTO;
import com.barbearia.estilos.appointments.services.AppointmentService;
import com.barbearia.estilos.services.services.ServiceItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final int DAYS_AHEAD = 6;
  private static final String PARAM_ADMIN_KEY = "adminKey";

  private final AppointmentService appointmentService;
  private final ServiceItemService serviceItemService;

  public AdminController(AppointmentService appointmentService, ServiceItemService serviceItemService) {
    this.appointmentService = appointmentService;
    this.serviceItemService = serviceItemService;
  }

  @GetMapping
  public String dashboard(@RequestParam(value = PARAM_ADMIN_KEY, required = false) String adminKey, Model model) {
    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.plusDays(DAYS_AHEAD).atTime(LocalTime.MAX);

    model.addAttribute("appointments", appointmentService.findByDateRange(start, end));
    model.addAttribute("services", serviceItemService.findAll());
    model.addAttribute(PARAM_ADMIN_KEY, adminKey);
    return "admin/dashboard";
  }

  @PostMapping("/slots")
  public String generateSlots(
      @Valid @ModelAttribute GenerateSlotsDTO dto,
      @RequestParam(PARAM_ADMIN_KEY) String adminKey,
      RedirectAttributes redirectAttributes)
  {
    appointmentService.generateSlots(dto);
    redirectAttributes.addFlashAttribute("successMessage", "Slots successfully generated!");
    return "redirect:/admin?adminKey=" + adminKey;
  }

  @PostMapping("/appointments/{id}/cancel")
  public String cancelAppointment(
      @PathVariable("id") UUID id,
      @RequestParam(PARAM_ADMIN_KEY) String adminKey,
      RedirectAttributes redirectAttributes
  ) {
    appointmentService.cancel(id);
    redirectAttributes.addFlashAttribute("successMessage", "Appointment cancelled successfully.");
    return "redirect:/admin?adminKey=" + adminKey;
  }
}