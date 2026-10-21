package com.barbearia.estilos.services.controllers;

import com.barbearia.estilos.services.services.ServiceItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceItemController.class)
class ServiceItemControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ServiceItemService serviceItemService;

  @DisplayName("createServiceSuccess - Should create service and redirect back to admin")
  @Test
  void createServiceSuccess() throws Exception {
    when(serviceItemService.create(any())).thenReturn(null);

    mockMvc.perform(post("/admin/services")
            .param("name", "Corte Tesoura")
            .param("amount", "50.0")
            .param("adminKey", "SILVA2024"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin?adminKey=SILVA2024"));
  }

  @DisplayName("deleteServiceSuccess - Should delete service and redirect back to admin")
  @Test
  void deleteServiceSuccess() throws Exception {
    UUID serviceId = UUID.randomUUID();
    org.mockito.Mockito.doNothing().when(serviceItemService).delete(any());

    mockMvc.perform(post("/admin/services/" + serviceId + "/delete")
            .param("adminKey", "SILVA2024"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin?adminKey=SILVA2024"));
  }

  @DisplayName("updateServiceSuccess - Should update service and redirect back to admin")
  @Test
  void updateServiceSuccess() throws Exception {
    UUID serviceId = UUID.randomUUID();
    when(serviceItemService.update(eq(serviceId), any())).thenReturn(null);

    mockMvc.perform(post("/admin/services/" + serviceId)
            .param("name", "Barba Premium")
            .param("amount", "35.0")
            .param("adminKey", "SILVA2024"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin?adminKey=SILVA2024"));
  }
}