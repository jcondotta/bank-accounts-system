package com.jcondotta.banking.recipients.infrastructure.bankaccount.adapters.input.rest.common.exception_handler;

import com.jcondotta.domain.exception.DomainNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.URI;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class ResourceNotFoundExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(DomainNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleResourceNotFound(
    DomainNotFoundException ex,
    HttpServletRequest request) {

    var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setType(ProblemTypes.RESOURCE_NOT_FOUND);
    problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
    problemDetail.setDetail(ex.getMessage());
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(problemDetail);
  }
}
