package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

sealed abstract class PrimitiveArrayType<SELF extends PrimitiveArrayType<SELF, T>, T> extends Type<SELF> implements ArrayValueType<SELF, T> permits BoolArrayType, Float32ArrayType, Float64ArrayType, Int16ArrayType, Int32ArrayType, Int64ArrayType, Int8ArrayType, Uint16ArrayType, Uint32ArrayType, Uint8ArrayType {
  @Nullable
  private final Expression lengthExpression;
  @Nullable
  private final Expression byteLengthExpression;

  private final List<LengthListener> lengthListeners = new ArrayList<>();
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final Primitive unitSize;

  public PrimitiveArrayType(int position, Primitive unitSize, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position);
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    this.unitSize = unitSize;
  }

  protected void checkConstant() {
    if (lengthExpression == null && byteLengthExpression == null && !isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot both be null unless there is a constantValue");
    } else if ((lengthExpression != null || byteLengthExpression != null) && isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot be set when value is constant.");
    }
  }

  @Override
  public boolean isConstant(Type<?> parent) {
    return ArrayValueType.super.isConstant(parent);
  }

  @Override
  public T @Nullable [] getConstantValue() {
    throw new UnsupportedOperationException("getConstantValue not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "ConstantValue(Pointer) instead.");
  }

  public final @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  @Override
  public final @Nullable Expression getLengthExpression() {
    return lengthExpression;
  }

  @Override
  public List<LengthListener> getLengthListeners() {
    return lengthListeners;
  }

  @Override
  public List<ValueChangeListener> getValueChangeListeners() {
    return valueChangeListeners;
  }

  public final Primitive getUnitSize() {
    return unitSize;
  }

  @Override
  public long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    offset += index * unitSize.size();
    return offset;
  }

  @Override
  public long getElementAllocationLength() {
    return unitSize.size();
  }

  @Override
  public long getAllocationLength(@Nullable Type<?> parent) {
    return ArrayValueType.super.getAllocationLength(parent);
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    return getLength(pointer) * unitSize.size();
  }

  @Override
  public final long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    return unitSize.size();
  }

  @Override
  public final long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return getUnitSize().size() * length;
  }

  @Override
  public boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    return ArrayValueType.super.isFixedLength(pointer);
  }

  @Override
  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("checkForConstantValue(Pointer, long, T) not supported for " + getClass().getSimpleName() + ". Use checkForConstantValue(Pointer, long, " + unitSize.wrapper() + ") instead.");
  }

  @Override
  public void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    throw new UnsupportedOperationException("checkForConstantValues(Pointer, long, T[]) not supported for " + getClass().getSimpleName() + ". Use checkForConstantValues(Pointer, long, " + unitSize.wrapper() + "[]) instead.");
  }

  @Override
  public void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    throw new UnsupportedOperationException("checkForConstantValues(Pointer, long, List<T>) not supported for " + getClass().getSimpleName() + ". Use checkForConstantValues(Pointer, long, List<" + unitSize.wrapper() + ">) instead.");
  }

  @Override
  public final T[] get(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("get(Pointer) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "(Pointer) instead.");
  }

  @Override
  public final T get(Pointer<?, ? extends Type<?>> pointer, long index) {
    throw new UnsupportedOperationException("get(Pointer, long) not supported for " + getClass().getSimpleName() + ". Use get" + unitSize.title() + "(Pointer, long) instead.");
  }

  @Override
  public T[] get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
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
}
