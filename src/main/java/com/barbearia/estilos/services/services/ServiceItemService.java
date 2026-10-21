package com.barbearia.estilos.services.services;

import com.barbearia.estilos.services.dtos.ServiceItemRequestDTO;
import com.barbearia.estilos.services.models.ServiceItem;
import com.barbearia.estilos.services.repositories.ServiceItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceItemService {

  private final ServiceItemRepository serviceItemRepository;

  public ServiceItemService(ServiceItemRepository serviceItemRepository) {
    this.serviceItemRepository = serviceItemRepository;
  }

  @Transactional(readOnly = true)
  public List<ServiceItem> findAll() {
    return serviceItemRepository.findAll();
  }

  @Transactional(readOnly = true)
  public ServiceItem findById(UUID id) {
    return serviceItemRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Service not found"));
  }

  @Transactional
  public ServiceItem create(ServiceItemRequestDTO dto) {
    if (serviceItemRepository.existsByName(dto.name().trim())) {
      throw new IllegalArgumentException("Service with name '" + dto.name() + "' already exists");
    }

    ServiceItem serviceItem = new ServiceItem(dto.name(), dto.amount());
    return serviceItemRepository.save(serviceItem);
  }

  @Transactional
  public ServiceItem update(UUID id, ServiceItemRequestDTO dto) {
    ServiceItem serviceItem = findById(id);
    serviceItem.updateDetails(dto.name(), dto.amount());
    return serviceItemRepository.save(serviceItem);
  }

  @Transactional
  public void delete(UUID id) {
    ServiceItem serviceItem = findById(id);
    serviceItemRepository.delete(serviceItem);
  }
}
