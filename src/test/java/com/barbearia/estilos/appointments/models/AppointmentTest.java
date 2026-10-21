package com.barbearia.estilos.appointments.models;

import com.barbearia.estilos.appointments.enums.AppointmentStatus;
import com.barbearia.estilos.services.models.ServiceItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

  private Appointment appointment;
  private ServiceItem serviceItem;

  @BeforeEach
  void setUp() {
    appointment = Appointment.createSlot(LocalDateTime.now().plusDays(1));
    serviceItem = new ServiceItem("Corte Degradê", 40.0);
  }

  @DisplayName("createSlotSuccess - Should create available slot successfully in the future")
  @Test
  void createSlotSuccess() {
    LocalDateTime futureDate = LocalDateTime.now().plusDays(2);
    Appointment slot = Appointment.createSlot(futureDate);

    assertEquals(AppointmentStatus.AVAILABLE, slot.getStatus());
    assertEquals(futureDate, slot.getDate());
    assertEquals("", slot.getClientNumber());
    assertEquals(0.0, slot.getAmount());
  }

  @DisplayName("createSlotError - Should throw exception when slot date is in the past")
  @Test
  void createSlotError() {
    LocalDateTime pastDate = LocalDateTime.now().minusDays(1);

    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> Appointment.createSlot(pastDate)
    );

    assertEquals("Cannot create appointment slot in the past", exception.getMessage());
  }

  @DisplayName("scheduleSuccess - Should schedule appointment successfully")
  @Test
  void scheduleSuccess() {
    appointment.schedule("11999999999", List.of(serviceItem));

    assertEquals(AppointmentStatus.SCHEDULED, appointment.getStatus());
    assertEquals("11999999999", appointment.getClientNumber());
    assertEquals(40.0, appointment.getAmount());
    assertEquals(1, appointment.getServices().size());
  }

  @DisplayName("scheduleErrorNotAvailable - Should throw exception when slot is not available")
  @Test
  void scheduleErrorNotAvailable() {
    appointment.schedule("11999999999", List.of(serviceItem));

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> appointment.schedule("11888888888", List.of(serviceItem))
    );

    assertEquals("Appointment slot is not available for scheduling", exception.getMessage());
  }

  @DisplayName("scheduleErrorInvalidClient - Should throw exception when client number is empty")
  @Test
  void scheduleErrorInvalidClient() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> appointment.schedule("   ", List.of(serviceItem))
    );

    assertEquals("Client WhatsApp number is required", exception.getMessage());
  }

  @DisplayName("scheduleErrorNoServices - Should throw exception when service list is empty")
  @Test
  void scheduleErrorNoServices() {
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> appointment.schedule("11999999999", List.of())
    );

    assertEquals("At least one service must be selected", exception.getMessage());
  }

  @DisplayName("completeSuccess - Should complete scheduled appointment successfully")
  @Test
  void completeSuccess() {
    appointment.schedule("11999999999", List.of(serviceItem));
    appointment.complete();

    assertEquals(AppointmentStatus.COMPLETED, appointment.getStatus());
  }

  @DisplayName("completeError - Should throw exception when appointment is not scheduled")
  @Test
  void completeError() {
    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> appointment.complete()
    );

    assertEquals("Only scheduled appointments can be completed", exception.getMessage());
  }

  @DisplayName("cancelSuccessFromScheduled - Should cancel scheduled appointment successfully")
  @Test
  void cancelSuccessFromScheduled() {
    appointment.schedule("11999999999", List.of(serviceItem));
    appointment.cancel();

    assertEquals(AppointmentStatus.CANCELLED, appointment.getStatus());
  }

  @DisplayName("cancelErrorCompleted - Should throw exception when trying to cancel completed appointment")
  @Test
  void cancelErrorCompleted() {
    appointment.schedule("11999999999", List.of(serviceItem));
    appointment.complete();

    IllegalStateException exception = assertThrows(
        IllegalStateException.class,
        () -> appointment.cancel()
    );

    assertEquals("Completed appointments cannot be cancelled", exception.getMessage());
  }
}