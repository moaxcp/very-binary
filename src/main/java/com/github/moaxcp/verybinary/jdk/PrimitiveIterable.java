package com.github.moaxcp.verybinary.jdk;

import java.util.PrimitiveIterator;

public interface PrimitiveIterable<T, CONSUMER> extends Iterable<T> {

  @Override
  PrimitiveIterator<T, CONSUMER> iterator();
}
