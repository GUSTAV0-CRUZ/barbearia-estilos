package com.barbearia.estilos.appointments.models;

import com.barbearia.estilos.appointments.enums.AppointmentStatus;
import com.barbearia.estilos.services.models.ServiceItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(
    name = "appointments",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_appointment_date_status",
            columnNames = {"date", "status"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "client_number", length = 20, nullable = false)
  private String clientNumber;

  @Column(nullable = false)
  private LocalDateTime date;

  @Column(nullable = false)
  private Double amount;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AppointmentStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToMany
  @JoinTable(
      name = "appointments_services",
      joinColumns = @JoinColumn(name = "id_appointments"),
      inverseJoinColumns = @JoinColumn(name = "id_services")
  )
  private Set<ServiceItem> services = new HashSet<>();

  public static Appointment createSlot(LocalDateTime date) {
    if (date.isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("Cannot create appointment slot in the past");
    }

    Appointment appointment = new Appointment();
    appointment.date = date;
    appointment.status = AppointmentStatus.AVAILABLE;
    appointment.clientNumber = "";
    appointment.amount = 0.0;
    return appointment;
  }

  public void schedule(String clientNumber, List<ServiceItem> selectedServices) {
    if (this.status != AppointmentStatus.AVAILABLE) {
      throw new IllegalStateException("Appointment slot is not available for scheduling");
    }
    validateClientNumber(clientNumber);
    validateServices(selectedServices);

    this.clientNumber = clientNumber.trim();
    this.services = new HashSet<>(selectedServices);
    this.amount = calculateTotalAmount();
    this.status = AppointmentStatus.SCHEDULED;
  }

  public void complete() {
    if (this.status != AppointmentStatus.SCHEDULED) {
      throw new IllegalStateException("Only scheduled appointments can be completed");
    }
    this.status = AppointmentStatus.COMPLETED;
  }

  public void cancel() {
    if (this.status == AppointmentStatus.COMPLETED) {
      throw new IllegalStateException("Completed appointments cannot be cancelled");
    }
    this.status = AppointmentStatus.CANCELLED;
  }

  public Double calculateTotalAmount() {
    return this.services.stream()
        .mapToDouble(ServiceItem::getAmount)
        .sum();
  }

  private void validateClientNumber(String clientNumber) {
    if (clientNumber == null || clientNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("Client WhatsApp number is required");
    }
  }

  private void validateServices(List<ServiceItem> services) {
    if (services == null || services.isEmpty()) {
      throw new IllegalArgumentException("At least one service must be selected");
    }
  }
}