package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.FLOAT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Float64Type extends NumberType<Float64Type, Double> {

  public static Float64Type float64Type() {
    return float64Type(-1);
  }

  public static Float64Type float64Type(int position) {
    return new Float64Type(position);
  }

  public Float64Type(int position, @Nullable Double constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, FLOAT64, constantValue, lengthExpression, byteLengthExpression);
  }

  public Float64Type(int position) {
    super(position, FLOAT64);
  }

  @Override
  public Float64Type copy(int position) {
    return new Float64Type(position, constantValue, lengthExpression, byteLengthExpression);
  }

  public double getFloat64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat64(getOffset(pointer));
  }

  public double getFloat64(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getFloat64(getOffset(pointer, index));
  }

  public double[] getFloat64Array(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getFloat64(getOffset(pointer), length);
  }

  public double[] getFloat64Array(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getFloat64(getOffset(pointer, index), length);
  }

  public List<Double> getFloat64List(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getFloat64List(getOffset(pointer), length);
  }

  public List<Double> getFloat64List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getFloat64List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, double value) {
    checkForConstantValue(pointer, 0, value);
    setUnchecked(SET_VALUE, pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, double... values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<Double> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, double value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, double... values) {
    checkArrayRange(pointer, index, index + values.length);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Double> values) {
    checkArrayRange(pointer, index, index + values.size());
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, (double) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, 0, (double) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, double value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getFloat64(getOffset(pointer, index));
      pointer.getByteArray().setFloat64(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, index, old, value);
    }
    pointer.getByteArray().setFloat64(getOffset(pointer, index), value);
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, double value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, double... values) {
    //todo implement value change listener
    pointer.getByteArray().setFloat64(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, double[] values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Double> values) {
    //todo implement value change listener
    pointer.getByteArray().setFloat64(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Double> values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, double value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, double... values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Double> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, double value) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + 1);
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, double... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.length);
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Double> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.size());
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isArray()) {
      long length = getArrayLength(pointer);
      for (int i = 0; i < length; i++) {
        pointer.getByteArray().addFloat64(getOffset(pointer, i), constantValue != null ? constantValue : 0.0d);
      }
    } else {
      pointer.getByteArray().addFloat64(getOffset(pointer, 0), constantValue != null ? constantValue : 0.0d);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addFloat64(getOffset(pointer, index), constantValue != null ? constantValue : 0.0d);
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var values = new double[Math.toIntExact(length)];
        for (int i = 0; i < length; i++) {
          values[i] = constantValue != null ? constantValue : 0.0d;
        }
        pointer.getByteArray().addFloat64(getOffset(pointer, index), values);
      });
    });
  }
}
