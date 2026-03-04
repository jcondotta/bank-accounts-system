package com.jcondotta.domain.model;

public interface EntityId<T> {
  T value();

  default String asString() {
    return value().toString();
  }
}
