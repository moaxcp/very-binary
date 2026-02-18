package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT16;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Int16ArrayType extends PrimitiveArrayType<Int16ArrayType, Short> {

  private final short @Nullable [] constantValue;

  public Int16ArrayType(int position, short @Nullable [] constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, INT16, lengthExpression, byteLengthExpression);
    this.constantValue = constantValue;
  }

  @Override
  public Int16ArrayType copy(int position) {
    return new Int16ArrayType(position, constantValue, getLengthExpression(), getByteLengthExpression());
  }

  public short[] getInt16(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt16(getOffset(pointer), length);
  }

  public short getInt16(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getInt16(getOffset(pointer, index));
  }

  public short[] getInt16(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt16(getOffset(pointer, index), length);
  }

  public List<Short> getInt16List(Pointer<?, ? extends Type<?>> pointer) {
    var length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt16List(getOffset(pointer), length);
  }

  public List<Short> getInt16List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt16List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, short[] values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<Short> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, short value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, short[] values) {
    checkArrayRange(pointer, index, index + values.length - 1);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Short> values) {
    checkArrayRange(pointer, index, index + values.size() - 1);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, short value) {
    if (pointer.getByteArray().getInt16(getOffset(pointer, index)) == value) {
      return;
    }
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt16(getOffset(pointer, index), getLength(pointer));
      var newValue = Arrays.copyOf(old, old.length);
      pointer.getByteArray().setInt16(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setInt16(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, short[] values) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt16(getOffset(pointer), getLength(pointer));
      var newValue = new short[old.length + values.length];
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
        pointer.getByteArray().setInt16(getOffset(pointer, index), values);
        notifyValueChange(reason, pointer, old, newValue);
      }
    } else {
      pointer.getByteArray().setInt16(getOffset(pointer, index), values);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Short> values) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt16List(getOffset(pointer), getLength(pointer));
      var newValue = new ArrayList<Short>(values.size());
      newValue.addAll(old.subList(0, Math.toIntExact(index)));
      newValue.addAll(values);
      newValue.addAll(old.subList(Math.toIntExact(index), old.size()));
      pointer.getByteArray().setInt16(getOffset(pointer, index), values);
      if (!old.equals(newValue)) {
        notifyValueChange(reason, pointer, old, newValue);
      }
    } else {
      pointer.getByteArray().setInt16(getOffset(pointer, index), values);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, short value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue[Math.toIntExact(index)], value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue[Math.toIntExact(index)]);
    }
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, short[] values) {
    if (isConstantValue(pointer.getType())) {
      if (constantValue.length != values.length) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + Arrays.toString(values) + " constant: " + Arrays.toString(constantValue));
      }
      for (var i = 0; i < values.length; i++) {
        if (!Objects.equals(constantValue[i], values[i])) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values[i] + " constant: " + constantValue[i]);
        }
      }
    }
  }

  public void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Short> values) {
    if (isConstantValue(pointer.getType())) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, short value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, short[] values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Short> values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, short value) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, short[] values) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Short> values) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isConstantValue(pointer.getType())) {
      pointer.getByteArray().addInt16(getOffset(pointer), constantValue);
    } else {
      long length = getByteLength(pointer);
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), length);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    if (isConstantValue(pointer.getType())) {
      throw new IllegalStateException("Cannot allocate element in constantValue " + Arrays.toString(constantValue));
    }
    callWithLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), getUnitSize().size());
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (isConstantValue(pointer.getType())) {
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
