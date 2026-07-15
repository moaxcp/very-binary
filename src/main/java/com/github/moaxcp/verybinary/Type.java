package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

public sealed interface Type<SELF extends Type<SELF>> permits AbstractType, ComplexType {
  int getPosition();

  <V extends ComplexType<V>> @Nullable ComplexType<V> getParent();

  /**
   * true if the bytes for this type are always constant.
   *
   * @return
   */
  boolean isConstant();

  long getOffset(Pointer<?, ? extends Type<?>> pointer);

  long getAllocationLength();

  long getByteLength(Pointer<?, ? extends Type<?>> pointer);

  /**
   * Returns true of the byte length of this type is constant.
   *
   * @return
   */
  boolean isFixedLength();

  void allocate(Pointer<?, ? extends Type<?>> pointer);

  void notifyByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previousLength, long currentLength);

  void callWithByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Runnable runnable);
}
