package com.jcondotta.bankaccounts.infrastructure.adapters.input.rest.common.exception_handler;

import com.jcondotta.bankaccounts.domain.exceptions.DomainNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class ResourceNotFoundExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(DomainNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleResourceNotFound(HttpServletRequest request) {

    var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
    problemDetail.setType(ProblemTypes.RESOURCE_NOT_FOUND);
    problemDetail.setTitle(HttpStatus.NOT_FOUND.getReasonPhrase());
    problemDetail.setInstance(URI.create(request.getRequestURI()));

    return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
      .body(problemDetail);
  }
}
