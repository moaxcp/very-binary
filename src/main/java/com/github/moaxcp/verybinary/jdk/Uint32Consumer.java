package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Uint32Consumer {
  void accept(long value);

  default Uint32Consumer andThen(Uint32Consumer after) {
    Objects.requireNonNull(after);
    return (long t) -> { accept(t); after.accept(t); };
  }
}
