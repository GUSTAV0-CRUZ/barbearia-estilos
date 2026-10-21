package com.barbearia.estilos.appointments.controllers;

import com.barbearia.estilos.appointments.services.AppointmentService;
import com.barbearia.estilos.services.services.ServiceItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AppointmentService appointmentService;

  @MockitoBean
  private ServiceItemService serviceItemService;

  @DisplayName("dashboardSuccess - Should load dashboard when adminKey is valid")
  @Test
  void dashboardSuccess() throws Exception {
    when(appointmentService.findByDateRange(any(), any())).thenReturn(List.of());
    when(serviceItemService.findAll()).thenReturn(List.of());

    // Nota: A chave configurada no application.yaml por padrão é SILVA2024
    mockMvc.perform(get("/admin").param("adminKey", "SILVA2024"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/dashboard"))
        .andExpect(model().attribute("adminKey", "SILVA2024"))
        .andExpect(model().attribute("hasError", false));
  }

  @DisplayName("dashboardError - Should set error flag when adminKey is invalid")
  @Test
  void dashboardError() throws Exception {
    when(appointmentService.findByDateRange(any(), any())).thenReturn(List.of());
    when(serviceItemService.findAll()).thenReturn(List.of());

    mockMvc.perform(get("/admin").param("adminKey", "CHAVE_ERRADA"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/dashboard"))
        .andExpect(model().attribute("hasError", true));
  }

  @DisplayName("loginSuccess - Should redirect to admin with key when valid")
  @Test
  void loginSuccess() throws Exception {
    mockMvc.perform(get("/admin/login").param("adminKey", "SILVA2024"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin?adminKey=SILVA2024"));
  }

  @DisplayName("loginError - Should redirect with error when login key is invalid")
  @Test
  void loginError() throws Exception {
    mockMvc.perform(get("/admin/login").param("adminKey", "KEY_ERROR"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin?error=true"));
  }
}
