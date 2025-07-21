package com.leminhbao.eventmanagement.config;

import com.leminhbao.eventmanagement.dto.common.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseEnhancer implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    // Only enhance ApiResponse objects
    return returnType.getParameterType().equals(ApiResponse.class) ||
        (returnType.getGenericParameterType().toString().contains("ApiResponse"));
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

    if (body instanceof ApiResponse<?> apiResponse && request instanceof ServletServerHttpRequest servletRequest) {
      // Extract request path from the servlet request
      HttpServletRequest httpRequest = servletRequest.getServletRequest();
      String requestPath = httpRequest.getRequestURI();

      // Update the path in the response
      apiResponse.setPath(requestPath);
    }

    return body;
  }
}
