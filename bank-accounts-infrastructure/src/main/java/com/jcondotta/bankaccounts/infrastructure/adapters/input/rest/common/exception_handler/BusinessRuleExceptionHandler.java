package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.exception_handler;

import com.jcondotta.bankaccounts.domain.exceptions.DomainRuleValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class BusinessRuleExceptionHandler {

  @ExceptionHandler(DomainRuleValidationException.class)
  public ResponseEntity<ProblemDetail> handleBusinessRuleViolation(DomainRuleValidationException ex, HttpServletRequest request) {
    log.warn(
      "Business rule violation at [{}]: {}",
      request.getRequestURI(),
      ex.getMessage(),
      ex
    );

    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    problemDetail.setType(ProblemTypes.BUSINESS_RULE_VIOLATION);
    problemDetail.setTitle("Operation not allowed");
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity
      .status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body(problemDetail);
  }
}
