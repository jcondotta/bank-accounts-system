package com.jcondotta.application.core;

@FunctionalInterface
public interface CommandHandlerWithResult<C, R> {

  R handle(C command);

}