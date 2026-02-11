package com.github.moaxcp.verybinary.jdk;

import com.github.moaxcp.verybinary.array.BoolArray.BoolArrayIterator;

import java.util.PrimitiveIterator;

public interface PrimitiveIterable<T, CONSUMER> extends Iterable<T> {

  @Override
  PrimitiveIterator<T, CONSUMER> iterator();
}
