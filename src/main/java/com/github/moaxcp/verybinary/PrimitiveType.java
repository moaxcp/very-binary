package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract sealed class PrimitiveType<SELF extends PrimitiveType<SELF, T>, T> extends Type<SELF> implements ValueType<SELF, T> permits BoolType, NumberType {

  @Nullable
  private final Expression byteLengthExpression;
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();
  private Primitive unitSize;

  PrimitiveType(int position, @Nullable Expression byteLengthExpression, Primitive unitSize) {
    super(position);
    this.byteLengthExpression = byteLengthExpression;
    this.unitSize = unitSize;
  }

  @Override
  public @Nullable T getConstantValue() {
    throw new UnsupportedOperationException("getConstantValue not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "ConstantValue(Pointer) instead.");
  }

  public @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  @Override
  public List<ValueChangeListener> getValueChangeListeners() {
    return valueChangeListeners;
  }

  @Override
  public List<ByteLengthListener> getByteLengthListeners() {
    return byteLengthListeners;
  }

  public final Primitive getUnitSize() {
    return unitSize;
  }

  @Override
  public long getAllocationLength(@Nullable Type<?> parent) {
    return unitSize.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    return unitSize.size();
  }

  @Override
  public final boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    return true;
  }

  @Override
  public final T get(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("get(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "(Pointer) instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("set(Pointer, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use set(Pointer, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("checkForConstantValue(Pointer, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use checkForConstantValue(Pointer, long, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    PrimitiveType<?, ?> that = (PrimitiveType<?, ?>) o;
    return Objects.equals(valueChangeListeners, that.valueChangeListeners) && unitSize == that.unitSize;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Objects.hashCode(valueChangeListeners);
    result = 31 * result + unitSize.hashCode();
    return result;
  }
}
