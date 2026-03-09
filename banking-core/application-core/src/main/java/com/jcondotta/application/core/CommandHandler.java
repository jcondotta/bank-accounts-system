package com.jcondotta.application.core;

@FunctionalInterface
public interface CommandHandler<C> {

  void handle(C command);
}