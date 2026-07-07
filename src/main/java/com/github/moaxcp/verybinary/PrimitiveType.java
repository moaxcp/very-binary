package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

public abstract sealed class PrimitiveType<SELF extends PrimitiveType<SELF, T>, T> extends BasicType<SELF, T> permits BoolType, Float32Type, Float64Type, Int16Type, Int32Type, Int64Type, Int8Type, Uint16Type, Uint32Type, Uint8Type {


  PrimitiveType(int position, @Nullable ComplexType<?> parent, BasicTypeInfo basicTypeInfo) {
    super(position, parent, basicTypeInfo, null);
  }

  @Override
  public @Nullable T getConstantValue() {
    throw new UnsupportedOperationException("getConstantValue not supported for " + getClass().getSimpleName() + ". Use get" + basicTypeInfo.title() + "ConstantValue(Pointer) instead.");
  }

  @Override
  public final T get(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("get(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + basicTypeInfo.title() + "(Pointer) instead.");
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("setUnchecked(Pointer, " + basicTypeInfo.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use setUnchecked(Pointer, " + basicTypeInfo.primitive() + ") instead.");
  }

  /**
   *
   * @param pointer
   * @throws UnsupportedOperationException because subclass should implement with primitive constant if possible
   */
  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("allocate(Pointer) not supported for " + getClass().getSimpleName() + ". Use allocate" + basicTypeInfo.title() + "(Pointer) instead.");
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    return true;
  }
}
