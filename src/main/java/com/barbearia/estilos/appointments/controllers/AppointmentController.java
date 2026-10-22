package com.barbearia.estilos.appointments.controllers;

import com.barbearia.estilos.appointments.dtos.ScheduleAppointmentDTO;
import com.barbearia.estilos.appointments.models.Appointment;
import com.barbearia.estilos.appointments.services.AppointmentService;
import com.barbearia.estilos.services.services.ServiceItemService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class AppointmentController {

  private static final int DAYS_AHEAD = 7;

  private final AppointmentService appointmentService;
  private final ServiceItemService serviceItemService;

  public AppointmentController(AppointmentService appointmentService, ServiceItemService serviceItemService) {
    this.appointmentService = appointmentService;
    this.serviceItemService = serviceItemService;
  }

  @GetMapping
  public String index(Model model) {
    LocalDate today = LocalDate.now();
    LocalDateTime start = today.atStartOfDay();
    LocalDateTime end = today.plusDays(DAYS_AHEAD).atTime(LocalTime.MAX);

    List<Appointment> slots = appointmentService.findByDateRange(start, end);

    Map<LocalDate, Long> vacanciesMap = new HashMap<>();
    for (int i = 0; i < DAYS_AHEAD; i++) {
      LocalDate day = today.plusDays(i);
      long availableCount = slots.stream()
          .filter(slot -> slot.getDate() != null && slot.getDate().toLocalDate().equals(day))
          .filter(slot -> slot.getStatus() != null && slot.getStatus().name().equals("AVAILABLE"))
          .count();
      vacanciesMap.put(day, availableCount);
    }

    model.addAttribute("selectedDate", today);
    model.addAttribute("vacanciesMap", vacanciesMap);
    return "index";
  }

  @GetMapping("/booking")
  public String booking(
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
      Model model
  ) {
    LocalDateTime start = date.atStartOfDay();
    LocalDateTime end = date.atTime(LocalTime.MAX);

    List<Appointment> daySlots = appointmentService.findByDateRange(start, end);
    model.addAttribute("daySlots", daySlots);
    model.addAttribute("services", serviceItemService.findAll());
    model.addAttribute("selectedDate", date);
    return "booking";
  }

  @PostMapping("/book")
  public String book(@Valid @ModelAttribute ScheduleAppointmentDTO dto, RedirectAttributes redirectAttributes) {
    Appointment appointment = appointmentService.schedule(dto);
    redirectAttributes.addFlashAttribute("confirmedAppointment", appointment);
    redirectAttributes.addFlashAttribute("showModal", true);
    return "redirect:/booking?date=" + appointment.getDate().toLocalDate();
  }
}