package com.barbearia.estilos.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

  @Value("${app.barbershop.name}")
  private String barbershopName;

  @Value("${app.barber.name}")
  private String barberName;

  @ModelAttribute("barbershopName")
  public String getBarbershopName() {
    return barbershopName;
  }

  @ModelAttribute("barberName")
  public String getBarberName() {
    return barberName;
  }
}