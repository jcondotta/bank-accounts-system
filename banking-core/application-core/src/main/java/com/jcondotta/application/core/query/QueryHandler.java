package com.jcondotta.application.core.query;

@FunctionalInterface
public interface QueryHandler<Q extends Query<R>, R> {

  R handle(Q query);

}