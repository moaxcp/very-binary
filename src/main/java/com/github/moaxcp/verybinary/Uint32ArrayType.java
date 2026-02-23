package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint32ArrayType extends PrimitiveArrayType<Uint32ArrayType, Long> {

  private final long @Nullable [] constantValue;

  public Uint32ArrayType(int position, long @Nullable [] constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, UINT32, lengthExpression, byteLengthExpression);
    this.constantValue = constantValue;
    checkConstant();
  }

  @Override
  public Uint32ArrayType copy(int position) {
    return new Uint32ArrayType(position, constantValue, getLengthExpression(), getByteLengthExpression());
  }

  @Override
  public boolean isConstant() {
    return constantValue != null;
  }

  @Override
  public long getConstantValueSize() {
    return constantValue != null ? constantValue.length : 0;
  }

  public long[] getUint32(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getUint32(getOffset(pointer), length);
  }

  public long getUint32(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getUint32(getOffset(pointer, index));
  }

  public long[] getUint32(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint32(getOffset(pointer, index), length);
  }

  public List<Long> getUint32List(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getUint32List(getOffset(pointer), length);
  }

  public List<Long> getUint32List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint32List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long[] values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<Long> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, long[] values) {
    checkArrayRange(pointer, index, index + values.length);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Long> values) {
    checkArrayRange(pointer, index, index + values.size());
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    if (pointer.getByteArray().getUint32(getOffset(pointer, index)) == value) {
      return;
    }
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint32(getOffset(pointer, index), getLength(pointer));
      var newValue = Arrays.copyOf(old, old.length);
      pointer.getByteArray().setUint32(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint32(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long[] values) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint32(getOffset(pointer), getLength(pointer));
      var newValue = new long[old.length + values.length];
      if (index == 0) {
        System.arraycopy(values, 0, newValue, 0, values.length);
        System.arraycopy(old, 0, newValue, values.length, old.length);
      } else if (index == old.length) {
        System.arraycopy(old, 0, newValue, 0, old.length);
        System.arraycopy(values, 0, newValue, old.length, values.length);
      } else {
        System.arraycopy(old, 0, newValue, 0, (int) index);
        System.arraycopy(values, 0, newValue, (int) index, values.length);
        System.arraycopy(old, (int) index, newValue, (int) index + values.length, old.length - (int) index);
      }
      if (!Arrays.equals(old, newValue)) {
        pointer.getByteArray().setUint32(getOffset(pointer, index), values);
        notifyValueChange(reason, pointer, old, newValue);
      }
    } else {
      pointer.getByteArray().setUint32(getOffset(pointer, index), values);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Long> values) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint32List(getOffset(pointer), getLength(pointer));
      var newValue = new ArrayList<Long>(values.size());
      newValue.addAll(old.subList(0, Math.toIntExact(index)));
      newValue.addAll(values);
      newValue.addAll(old.subList(Math.toIntExact(index), old.size()));
      pointer.getByteArray().setUint32(getOffset(pointer, index), values);
      if (!old.equals(newValue)) {
        notifyValueChange(reason, pointer, old, newValue);
      }
    } else {
      pointer.getByteArray().setUint32(getOffset(pointer, index), values);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    if (this.isConstant() && !Objects.equals(constantValue[Math.toIntExact(index)], value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue[Math.toIntExact(index)]);
    }
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, long[] values) {
    if (this.isConstant()) {
      if (constantValue.length != values.length) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + Arrays.toString(values) + " constant: " + Arrays.toString(constantValue));
      }
      for (var i =0; i < values.length; i++) {
        if (!Objects.equals(constantValue[i], values[i])) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values + " constant: " + constantValue[i]);
        }
      }
    }
  }

  public void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Long> values) {
    if (this.isConstant()) {
      if (constantValue.length != values.size()) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values + " constant: " + Arrays.toString(constantValue));
      }
      for (var i = 0; i < values.size(); i++) {
        if (!Objects.equals(constantValue[i], values.get(i))) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values.get(i) + " constant: " + constantValue[i]);
        }
      }
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long[] values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Long> values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, long[] values) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Long> values) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (this.isConstant()) {
      pointer.getByteArray().addUint32(getOffset(pointer), constantValue);
    } else {
      long length = getByteLength(pointer);
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), length);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), getUnitSize().size());
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (this.isConstant()) {
      throw new IllegalStateException("Cannot allocate element in constantValue " + Arrays.toString(constantValue));
    }
    callWithLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), getUnitSize().size() * length);
      });
    });
  }
}
