package com.barbearia.estilos.services.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ServiceItemRequestDTO(
    @NotBlank(message = "Service name is required")
    String name,

    @NotNull(message = "Service amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    Double amount
) {}