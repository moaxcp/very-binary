package com.github.moaxcp.verybinary;

public abstract sealed class NumberType<SELF extends NumberType<SELF, T>, T extends Number> extends PrimitiveType<SELF, T> permits Float32Type, Float64Type, Int16Type, Int32Type, Int64Type, Int8Type, Uint16Type, Uint32Type, Uint64Type, Uint8Type {

  NumberType(int position, Primitive unitSize) {
    super(position, unitSize);
  }

  public abstract long defaultArrayLengthValue();

  abstract void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value);

  abstract void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value);
}
