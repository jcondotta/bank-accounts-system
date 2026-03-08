package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.input.rest.common.exception_handler;

import com.jcondotta.domain.exception.DomainRuleValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class RuleValidationExceptionHandler {

  @ExceptionHandler(DomainRuleValidationException.class)
  public ResponseEntity<ProblemDetail> handleBusinessRuleViolation(
    DomainRuleValidationException ex,
    HttpServletRequest request
  ) {
    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
    problemDetail.setType(ProblemTypes.BUSINESS_RULE_VIOLATION);
    problemDetail.setTitle("Operation not allowed");
    problemDetail.setDetail(ex.getMessage());
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity
      .status(HttpStatus.UNPROCESSABLE_ENTITY)
      .body(problemDetail);
  }
}
