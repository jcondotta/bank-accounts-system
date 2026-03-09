package com.jcondotta.application.core;

public interface CommandBus {

  <R, C extends Command<R>> R execute(C command);

}