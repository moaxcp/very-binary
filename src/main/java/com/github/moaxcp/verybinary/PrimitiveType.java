package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract sealed class PrimitiveType<SELF extends PrimitiveType<SELF, T>, T> extends Type<SELF> implements ValueType<SELF, T> permits BoolType, NumberType {
  @Nullable
  protected final Expression lengthExpression;
  @Nullable
  protected final Expression byteLengthExpression;
  @Nullable
  protected final T constantValue;

  protected final List<ArrayLengthListener> arrayLengthListeners = new ArrayList<>();
  protected final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  protected Primitive unitSize;

  public PrimitiveType(int position, Primitive size) {
    super(position);
    this.lengthExpression = null;
    this.byteLengthExpression = null;
    this.constantValue = null;
    this.unitSize = size;
  }

  PrimitiveType(int position, Primitive unitSize, T constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position);
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    this.constantValue = constantValue;
    this.unitSize = unitSize;
  }

  public @Nullable T getConstantValue() {
    return constantValue;
  }

  public final @Nullable Expression getLengthExpression() {
    return lengthExpression;
  }

  public final @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  @Override
  public List<ArrayLengthListener> getArrayLengthListeners() {
    return arrayLengthListeners;
  }

  @Override
  public List<ValueChangeListener> getValueChangeListeners() {
    return valueChangeListeners;
  }

  public SELF addArrayLengthChangeListeners(List<ArrayLengthListener> arrayLengthChange) {
    arrayLengthListeners.addAll(arrayLengthChange);
    return (SELF) this;
  }

  public SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    this.valueChangeListeners.addAll(valueChangeListeners);
    return (SELF) this;
  }

  public SELF addValueChangeListener(ValueChangeListener listener) {
    this.valueChangeListeners.add(listener);
    return (SELF) this;
  }

  public final Primitive getUnitSize() {
    return unitSize;
  }

  @Override
  public long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    offset += index * getUnitSize().size();
    return offset;
  }

  @Override
  public long getAllocationLength(@Nullable Type<?> parent) {
    if (isArray()) {
      if (lengthExpression.isConstant(parent)) {
        return lengthExpression.constantValue(parent) * unitSize.size();
      } else {
        return lengthExpression.defaultValue(parent) * unitSize.size();
      }
    }
    return unitSize.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    if(!isArray()) {
      return unitSize.size();
    }
    return getArrayLength(pointer) * getUnitSize().size();
  }

  @Override
  public final long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    return getUnitSize().size();
  }

  @Override
  public final long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return getUnitSize().size() * length;
  }

  public final long getArrayLength(Pointer<?, ? extends Type<?>> pointer) {
    if (!isArray()) {
      return 1;
    }
    if (lengthExpression != null) {
      return lengthExpression.evaluate(pointer);
    }
    if (byteLengthExpression != null) {
      return byteLengthExpression.evaluate(pointer) / unitSize.size();
    }
    throw new IllegalStateException("lengthExpression and byteLengthExpression are both null");
  }

  @Override
  public final boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    return lengthExpression == null || lengthExpression.isConstant(pointer.getType());
  }

  @Override
  public final T get(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("get(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "(Pointer) instead.");
  }

  @Override
  public final T get(Pointer<?, ? extends Type<?>> pointer, long index) {
    throw new UnsupportedOperationException("get(Pointer, long) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "(Pointer, long) instead.");
  }

  @Override
  public T[] getArray(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("getArray(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "Array(Pointer) instead.");
  }

  @Override
  public T[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    throw new UnsupportedOperationException("getArray(Pointer, long, long) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "Array(Pointer, long, long) instead.");
  }

  @Override
  public List<T> getList(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("getList(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "List(Pointer) instead.");
  }

  @Override
  public List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    throw new UnsupportedOperationException("getList(Pointer, long, long) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "List(Pointer, long, long) instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("set(Pointer, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use set(Pointer, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("set(Pointer, long, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use set(Pointer, long, " + unitSize.primitive() + ") instead.");
  }

  @SafeVarargs
  @Override
  public final void set(Pointer<?, ? extends Type<?>> pointer, T... values) {
    throw new UnsupportedOperationException("set(Pointer, " + unitSize.wrapper() + "...) not supported for " + getClass().getSimpleName() + ". Use set(Pointer, " + unitSize.primitive() + "...) instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, T... values) {
    throw new UnsupportedOperationException("set(Pointer, long, " + unitSize.wrapper() + "...) not supported for " + getClass().getSimpleName() + ". Use set(Pointer, long, " + unitSize.primitive() + "...) instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    throw new UnsupportedOperationException("set(Pointer, List<" + unitSize.wrapper() + ">) not supported for " + getClass().getSimpleName() + ". Use set(Pointer, List<" + unitSize.primitive() + ">) instead.");
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    throw new UnsupportedOperationException("set(Pointer, long, List<" + unitSize.wrapper() + ">) not supported for " + getClass().getSimpleName() + ". Use set(Pointer, long, List<" + unitSize.primitive() + ">) instead.");
  }

  @Override
  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("checkForConstantValue(Pointer, long, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use checkForConstantValue(Pointer, long, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("add(Pointer, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use add(Pointer, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("add(Pointer, long, " + unitSize.wrapper() + ") not supported for " + getClass().getSimpleName() + ". Use add(Pointer, long, " + unitSize.primitive() + ") instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, T... values) {
    throw new UnsupportedOperationException("add(Pointer, " + unitSize.wrapper() + "...) not supported for " + getClass().getSimpleName() + ". Use add(Pointer, " + unitSize.primitive() + "...) instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T... values) {
    throw new UnsupportedOperationException("add(Pointer, long, " + unitSize.wrapper() + "...) not supported for " + getClass().getSimpleName() + ". Use add(Pointer, long, " + unitSize.primitive() + "...) instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    throw new UnsupportedOperationException("add(Pointer, List<" + unitSize.wrapper() + ">) not supported for " + getClass().getSimpleName() + ". Use add(Pointer, List<" + unitSize.primitive() + ">) instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    throw new UnsupportedOperationException("add(Pointer, long, List<" + unitSize.wrapper() + ">) not supported for " + getClass().getSimpleName() + ". Use add(Pointer, long, List<" + unitSize.primitive() + ">) instead.");
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "unitSize=" + unitSize +
        ", lengthExpression=" + lengthExpression +
        ", constantValue=" + constantValue +
        ", byteLengthChangeListeners=" + byteLengthListeners +
        ", position=" + position +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    PrimitiveType<?, ?> that = (PrimitiveType<?, ?>) o;
    return Objects.equals(lengthExpression, that.lengthExpression) && Objects.equals(arrayLengthListeners, that.arrayLengthListeners) && Objects.equals(constantValue, that.constantValue) && unitSize == that.unitSize;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Objects.hashCode(lengthExpression);
    result = 31 * result + Objects.hashCode(arrayLengthListeners);
    result = 31 * result + Objects.hashCode(constantValue);
    result = 31 * result + unitSize.hashCode();
    return result;
  }
}
