package com.github.moaxcp.verybinary.jdk;

import java.util.Objects;

public interface Uint16Consumer {
  void accept(int value);

  default Uint16Consumer andThen(Uint16Consumer after) {
    Objects.requireNonNull(after);
    return (int t) -> { accept(t); after.accept(t); };
  }
}
