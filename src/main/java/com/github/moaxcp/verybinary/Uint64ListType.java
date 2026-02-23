package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.moaxcp.verybinary.Primitive.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Uint64ListType extends Type<Uint64ListType> implements ListValueType<Uint64ListType, BigInteger> {
  @Nullable
  private final Expression lengthExpression;
  @Nullable
  private final Expression byteLengthExpression;

  private final List<LengthListener> lengthListeners = new ArrayList<>();
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final Primitive unitSize;

  private final @Nullable List<BigInteger> constantValue;

  public Uint64ListType(int position, @Nullable List<BigInteger> constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position);
    this.constantValue = constantValue;
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    unitSize = UINT64;
    if (lengthExpression == null && byteLengthExpression == null && !isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot both be null unless there is a constantValue");
    } else if ((lengthExpression != null || byteLengthExpression != null) && isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot be set when value is constant.");
    }
  }

  @Override
  public Uint64ListType copy(int position) {
    return new Uint64ListType(position, constantValue, lengthExpression, byteLengthExpression);
  }

  @Override
  public boolean isConstant(@Nullable Type<?> parent) {
    return ListValueType.super.isConstant(parent);
  }

  @Nullable
  @Override
  public List<BigInteger> getConstantValue() {
    return constantValue;
  }

  @Override
  public long getConstantValueSize() {
    return constantValue != null ? constantValue.size() : 0;
  }

  public @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  @Override
  public @Nullable Expression getLengthExpression() {
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
    return ListValueType.super.getAllocationLength(parent);
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    return getLength(pointer) * unitSize.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    return unitSize.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return unitSize.size() * length;
  }

  @Override
  public boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    return ListValueType.super.isFixedLength(pointer);
  }

  public List<BigInteger> get(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getUint64List(getOffset(pointer), length);
  }

  public BigInteger get(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getUint64(getOffset(pointer, index));
  }

  public List<BigInteger> get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint64List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<BigInteger> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    checkArrayRange(pointer, index, index + values.size());
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (pointer.getByteArray().getUint64(getOffset(pointer, index)) == value) {
      return;
    }
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint64(getOffset(pointer, index), getLength(pointer));
      var newValue = Arrays.copyOf(old, old.length);
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint64List(getOffset(pointer), getLength(pointer));
      var newValue = new ArrayList<BigInteger>(values.size());
      newValue.addAll(old.subList(0, Math.toIntExact(index)));
      newValue.addAll(values);
      newValue.addAll(old.subList(Math.toIntExact(index), old.size()));
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
      if (!old.equals(newValue)) {
        notifyValueChange(reason, pointer, old, newValue);
      }
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (this.isConstant()) {
      pointer.getByteArray().addUint64(getOffset(pointer), constantValue);
    } else {
      long length = getByteLength(pointer);
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), length);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    if (this.isConstant()) {
      throw new IllegalStateException("Cannot allocate element in constantValue " + constantValue);
    }
    callWithLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), unitSize.size());
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (this.isConstant()) {
      throw new IllegalStateException("Cannot allocate element in constantValue " + constantValue);
    }
    callWithLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), unitSize.size() * length);
      });
    });
  }
}
