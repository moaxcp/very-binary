package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Int8Consumer {
  void accept(byte value);

  default Int8Consumer andThen(Int8Consumer after) {
    Objects.requireNonNull(after);
    return (byte t) -> { accept(t); after.accept(t); };
  }
}
