package com.barbearia.estilos.services.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceItemTest {

  @DisplayName("createServiceSuccess - Should create service successfully with valid data")
  @Test
  void createServiceSuccess() {
    ServiceItem service = new ServiceItem("Corte Degradê", 45.0);

    assertEquals("Corte Degradê", service.getName());
    assertEquals(45.0, service.getAmount());
  }

  @DisplayName("createServiceErrorNullName - Should throw exception when name is null")
  @Test
  void createServiceErrorNullName() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new ServiceItem(null, 45.0)
    );

    assertEquals("Service name cannot be empty", exception.getMessage());
  }

  @DisplayName("createServiceErrorEmptyName - Should throw exception when name is empty")
  @Test
  void createServiceErrorEmptyName() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new ServiceItem("   ", 45.0)
    );

    assertEquals("Service name cannot be empty", exception.getMessage());
  }

  @DisplayName("createServiceErrorInvalidAmount - Should throw exception when amount is zero or negative")
  @Test
  void createServiceErrorInvalidAmount() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> new ServiceItem("Barba", 0.0)
    );

    assertEquals("Service amount must be greater than zero", exception.getMessage());
  }

  @DisplayName("updateDetailsSuccess - Should update service details successfully")
  @Test
  void updateDetailsSuccess() {
    ServiceItem service = new ServiceItem("Corte", 30.0);
    service.updateDetails("Corte Completo", 50.0);

    assertEquals("Corte Completo", service.getName());
    assertEquals(50.0, service.getAmount());
  }
}