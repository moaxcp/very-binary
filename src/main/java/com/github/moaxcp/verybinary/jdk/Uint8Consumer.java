package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Uint8Consumer {
  void accept(short value);

  default Uint8Consumer andThen(Uint8Consumer after) {
    Objects.requireNonNull(after);
    return (short t) -> { accept(t); after.accept(t); };
  }
}
