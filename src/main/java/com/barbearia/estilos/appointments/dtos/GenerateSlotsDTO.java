package com.barbearia.estilos.appointments.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record GenerateSlotsDTO(
    @NotNull(message = "Date is required")
    LocalDate date,

    @NotNull(message = "Start time is required")
    LocalTime startTime,

    int totalSlots,
    int durationMinutes
) {}