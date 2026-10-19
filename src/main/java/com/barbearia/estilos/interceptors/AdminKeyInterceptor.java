package com.barbearia.estilos.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminKeyInterceptor implements HandlerInterceptor {

  @Value("${app.security.admin-key}")
  private String adminKey;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (!request.getRequestURI().startsWith("/admin")) return true;

    String headerKey = request.getHeader("X-Admin-Key");
    String paramKey = request.getParameter("adminKey");

    if ((headerKey != null && headerKey.equals(adminKey)) ||
        (paramKey != null && paramKey.equals(adminKey))) {
      return true;
    }

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write("Access Denied: Invalid or missing Admin Key");
    return false;
  }
}
