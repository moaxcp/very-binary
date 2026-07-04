package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.List;

public sealed interface Type<SELF extends Type<SELF>> permits AbstractType, ComplexType {
  int getPosition();

  <V extends ComplexType<V>> @Nullable ComplexType<V> getParent();

  List<ByteLengthListener> getByteLengthListeners();

  SELF addByteLengthListeners(List<ByteLengthListener> listeners);

  SELF addByteLengthListener(ByteLengthListener listener);

  SELF copy(int position, @Nullable ComplexType<?> parent);

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
