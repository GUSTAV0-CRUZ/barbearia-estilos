package com.barbearia.estilos.appointments.controllers;

import com.barbearia.estilos.appointments.services.AppointmentService;
import com.barbearia.estilos.services.services.ServiceItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AppointmentService appointmentService;

  @MockitoBean
  private ServiceItemService serviceItemService;

  @DisplayName("indexSuccess - Should load index page with vacancies map successfully")
  @Test
  void indexSuccess() throws Exception {
    when(appointmentService.findByDateRange(any(), any())).thenReturn(List.of());

    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attributeExists("selectedDate"))
        .andExpect(model().attributeExists("vacanciesMap"));
  }

  @DisplayName("bookingSuccess - Should load booking page with day slots and services")
  @Test
  void bookingSuccess() throws Exception {
    LocalDate today = LocalDate.now();
    when(appointmentService.findByDateRange(any(), any())).thenReturn(List.of());
    when(serviceItemService.findAll()).thenReturn(List.of());

    mockMvc.perform(get("/booking").param("date", today.toString()))
        .andExpect(status().isOk())
        .andExpect(view().name("booking"))
        .andExpect(model().attributeExists("daySlots"))
        .andExpect(model().attributeExists("services"))
        .andExpect(model().attributeExists("selectedDate"));
  }
}