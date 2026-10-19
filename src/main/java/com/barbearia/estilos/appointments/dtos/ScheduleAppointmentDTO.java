package com.barbearia.estilos.appointments.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ScheduleAppointmentDTO(
    @NotNull(message = "Appointment ID is required")
    UUID appointmentId,

    @NotBlank(message = "Client phone/WhatsApp number is required")
    String clientNumber,

    @NotEmpty(message = "At least one service must be selected")
    List<UUID> serviceIds
) {}