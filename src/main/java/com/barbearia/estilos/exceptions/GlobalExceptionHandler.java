package com.barbearia.estilos.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public String handleBusinessException(RuntimeException ex, RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
    return "redirect:/";
  }
}