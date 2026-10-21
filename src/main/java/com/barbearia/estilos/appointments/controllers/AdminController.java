package com.barbearia.estilos.appointments.controllers;

import com.barbearia.estilos.appointments.services.AppointmentService;
import com.barbearia.estilos.services.services.ServiceItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private static final int DAYS_AHEAD = 6;
  private static final String PARAM_ADMIN_KEY = "adminKey";

  @Value("${app.security.admin-key}")
  private String configuredAdminKey;

  @Value("${app.barbershop.name}")
  private String barbershopName;

  @Value("${app.barber.name}")
  private String barberName;

  private final AppointmentService appointmentService;
  private final ServiceItemService serviceItemService;

  public AdminController(AppointmentService appointmentService, ServiceItemService serviceItemService) {
    this.appointmentService = appointmentService;
    this.serviceItemService = serviceItemService;
  }

  @GetMapping
  public String dashboard(
      @RequestParam(value = PARAM_ADMIN_KEY, required = false) String adminKey,
      @RequestParam(value = "error", required = false) String error,
      Model model
  ) {

    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.plusDays(DAYS_AHEAD).atTime(LocalTime.MAX);

    boolean unlocked = adminKey != null && adminKey.equals(configuredAdminKey);
    boolean hasError = error != null || (adminKey != null && !unlocked);

    model.addAttribute("appointments", appointmentService.findByDateRange(start, end));
    model.addAttribute("services", serviceItemService.findAll());
    model.addAttribute("adminKey", unlocked ? adminKey : null);
    model.addAttribute("hasError", hasError);
    model.addAttribute("barbershopName", barbershopName);
    model.addAttribute("barberName", barberName);
    return "admin/dashboard";
  }

  @GetMapping("/login")
  public String login(@RequestParam(PARAM_ADMIN_KEY) String adminKey) {
    if (adminKey != null && adminKey.equals(configuredAdminKey)) {
      return "redirect:/admin?adminKey=" + adminKey;
    }
    return "redirect:/admin?error=true";
  }
}