package com.barbearia.estilos.services.repositories;

import com.barbearia.estilos.services.models.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ServiceItemRepository extends JpaRepository<ServiceItem, UUID> {
  boolean existsByName(String name);
}