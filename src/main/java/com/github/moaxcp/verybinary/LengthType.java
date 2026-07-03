package com.github.moaxcp.verybinary;

/**
 * A type that represents a length of a {@link ListType}.
 * @param <SELF>
 * @param <T>
 */
public sealed interface LengthType<SELF extends LengthType<SELF, T>, T extends Number> permits Float32Type, Float64Type, Int16Type, Int32Type, Int64Type, Int8Type, Uint16Type, Uint32Type, Uint64Type, Uint8Type {

  /**
   * Lists will always have a long index. This returns the default value for the length. It is typlically the constant
   * value converted to a long.
   * @return
   */
  long defaultLengthValue();

  /**
   * Sets the value of this field and notifies {@link LengthListener}s.
   * @param pointer
   * @param value
   */
  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value);

  /**
   * Sets the value of this field and notifies {@link ByteLengthListener}s.
   * @param pointer
   * @param value
   */
  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value);
}
