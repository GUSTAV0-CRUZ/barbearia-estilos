package com.barbearia.estilos.appointments.services;

import com.barbearia.estilos.appointments.dtos.GenerateSlotsDTO;
import com.barbearia.estilos.appointments.dtos.ScheduleAppointmentDTO;
import com.barbearia.estilos.appointments.enums.AppointmentStatus;
import com.barbearia.estilos.appointments.models.Appointment;
import com.barbearia.estilos.appointments.repositories.AppointmentRepository;
import com.barbearia.estilos.services.models.ServiceItem;
import com.barbearia.estilos.services.repositories.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final ServiceItemRepository serviceItemRepository;

  public AppointmentService(
      AppointmentRepository appointmentRepository,
      ServiceItemRepository serviceItemRepository
  ) {
    this.appointmentRepository = appointmentRepository;
    this.serviceItemRepository = serviceItemRepository;
  }

  @Transactional(readOnly = true)
  public List<Appointment> findByDateRange(LocalDateTime start, LocalDateTime end) {
    return appointmentRepository.findByDateBetween(start, end);
  }

  @Transactional(readOnly = true)
  public Appointment findById(UUID id) {
    return appointmentRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Appointment slot not found"));
  }

  @Transactional
  public List<Appointment> generateSlots(GenerateSlotsDTO dto) {
    List<Appointment> createdSlots = new ArrayList<>();
    LocalDateTime currentSlotTime = LocalDateTime.of(dto.date(), dto.startTime());

    for (int i = 0; i < dto.totalSlots(); i++) {
      boolean exists = appointmentRepository.existsByDateAndStatus(currentSlotTime, AppointmentStatus.AVAILABLE);

      if (exists) {
        currentSlotTime = currentSlotTime.plusMinutes(dto.durationMinutes());
        continue;
      }

      createdSlots.add(Appointment.createSlot(currentSlotTime));
      currentSlotTime = currentSlotTime.plusMinutes(dto.durationMinutes());
    }

    return appointmentRepository.saveAll(createdSlots);
  }

  @Transactional
  public Appointment schedule(ScheduleAppointmentDTO dto) {
    Appointment appointment = findById(dto.appointmentId());

    List<ServiceItem> services = serviceItemRepository.findAllById(dto.serviceIds());
    if (services.isEmpty()) {
      throw new IllegalArgumentException("No valid services found for the provided IDs");
    }

    appointment.schedule(dto.clientNumber(), services);
    return appointmentRepository.save(appointment);
  }

  @Transactional
  public void cancel(UUID appointmentId) {
    Appointment appointment = findById(appointmentId);
    appointment.cancel();
    appointmentRepository.save(appointment);
  }
}
