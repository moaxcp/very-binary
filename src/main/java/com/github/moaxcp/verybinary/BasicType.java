package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.Objects;

public abstract sealed class BasicType<SELF extends ValueType<SELF, T>, T> extends ValueType<SELF, T> permits PrimitiveType, Uint64Type {
  protected BasicTypeInfo basicTypeInfo;

  protected BasicType(int position, BasicTypeInfo basicTypeInfo, @Nullable T constantValue, @Nullable ComplexType<?> parent) {
    super(position, constantValue, parent);
    this.basicTypeInfo = basicTypeInfo;
  }

  public BasicTypeInfo getTypeInfo() {
    return basicTypeInfo;
  }

  @Override
  public long getAllocationLength() {
    return basicTypeInfo.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    return basicTypeInfo.size();
  }

  @Override
  public final boolean isFixedLength() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    BasicType<?, ?> basicType = (BasicType<?, ?>) o;
    return basicTypeInfo == basicType.basicTypeInfo;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Objects.hashCode(valueChangeListeners);
    result = 31 * result + basicTypeInfo.hashCode();
    return result;
  }
}
