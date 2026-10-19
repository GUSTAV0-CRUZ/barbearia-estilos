package com.barbearia.estilos.services.controllers;

import com.barbearia.estilos.services.dtos.ServiceItemRequestDTO;
import com.barbearia.estilos.services.services.ServiceItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin/services")
public class ServiceItemController {
  private static final String PARAM_ADMIN_KEY = "adminKey";

  private final ServiceItemService serviceItemService;

  public ServiceItemController(ServiceItemService serviceItemService) {
    this.serviceItemService = serviceItemService;
  }

  @PostMapping
  public String create(
      @Valid @ModelAttribute ServiceItemRequestDTO dto,
      @RequestParam(PARAM_ADMIN_KEY) String adminKey,
      RedirectAttributes redirectAttributes
  ) {
    serviceItemService.create(dto);
    redirectAttributes.addFlashAttribute("successMessage", "Service added to catalog successfully!");
    return "redirect:/admin?adminKey=" + adminKey;
  }

  @PostMapping("/{id}/delete")
  public String delete(
      @PathVariable("id") UUID id,
      @RequestParam(PARAM_ADMIN_KEY) String adminKey,
      RedirectAttributes redirectAttributes
  ) {
    serviceItemService.delete(id);
    redirectAttributes.addFlashAttribute("successMessage", "Service removed successfully.");
    return "redirect:/admin?adminKey=" + adminKey;
  }
}