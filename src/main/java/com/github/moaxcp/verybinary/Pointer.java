package com.github.moaxcp.verybinary;

public sealed interface Pointer<SELF extends Pointer<SELF, TYPE>, TYPE extends Type<TYPE>> permits ComplexPointer {

  long getOffset();

  SELF setOffset(long offset);

  long getByteLength();

  default boolean isFixedLength() {
    return getType().isFixedLength(this);
  }

  TYPE getType();

  int getPositions();

  ByteArray getByteArray();

  void setByteArray(ByteArray memory);

  void removeListener();
}
