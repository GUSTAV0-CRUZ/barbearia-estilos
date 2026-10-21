package com.barbearia.estilos.appointments.repositories;

import com.barbearia.estilos.appointments.enums.AppointmentStatus;
import com.barbearia.estilos.appointments.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
  List<Appointment> findByDateBetween(LocalDateTime start, LocalDateTime end);
  boolean existsByDateAndStatus(LocalDateTime date, AppointmentStatus status);
}