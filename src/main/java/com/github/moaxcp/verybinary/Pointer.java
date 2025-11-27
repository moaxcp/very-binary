package com.github.moaxcp.verybinary;

public interface Pointer<SELF extends Pointer<SELF, T>, T extends Type<?>> {
  SELF copy();

  long getOffset();

  SELF setOffset(long offset);

  long getByteLength();

  long getByteLength(int position);

  long getArrayLength(int i);

  T getType();

  <V extends Type<?>> V getType(int position);

  int getPositions();

  ByteArray getByteArray();

  void setByteArray(ByteArray memory);

  default boolean isFixedLength() {
    return getType().isFixedLength(this);
  }
}
