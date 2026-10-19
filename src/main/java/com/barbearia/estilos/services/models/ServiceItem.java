package com.barbearia.estilos.services.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "services")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServiceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(length = 100, nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  private Double amount;

  public ServiceItem(String name, Double amount) {
    validateName(name);
    validateAmount(amount);
    this.name = name.trim();
    this.amount = amount;
  }

  public void updateDetails(String newName, Double newAmount) {
    validateName(newName);
    validateAmount(newAmount);
    this.name = newName.trim();
    this.amount = newAmount;
  }

  private void validateName(String name) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Service name cannot be empty");
    }
  }

  private void validateAmount(Double amount) {
    if (amount == null || amount <= 0) {
      throw new IllegalArgumentException("Service amount must be greater than zero");
    }
  }
}