package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Float32Consumer {
  void accept(float value);

  default Float32Consumer andThen(Float32Consumer after) {
    Objects.requireNonNull(after);
    return (float t) -> { accept(t); after.accept(t); };
  }
}
