package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.FLOAT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_BY_ARRAY_LENGTH;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Float32Type extends NumberType<Float32Type, Float> {

  public static Float32Type float32Type() {
    return float32Type(-1);
  }

  public static Float32Type float32Type(int position) {
    return new Float32Type(position);
  }

  public Float32Type(int position, @Nullable Float constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, FLOAT32, constantValue, lengthExpression, byteLengthExpression);
  }

  public Float32Type(int position) {
    super(position, FLOAT32);
  }

  @Override
  public Float32Type copy(int position) {
    return new Float32Type(position, constantValue, lengthExpression, byteLengthExpression);
  }

  public float getFloat32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat32(getOffset(pointer));
  }

  public float getFloat32(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getFloat32(getOffset(pointer, index));
  }

  public float[] getFloat32Array(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getFloat32(getOffset(pointer), length);
  }

  public float[] getFloat32Array(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getFloat32(getOffset(pointer, index), length);
  }

  public List<Float> getFloat32List(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getFloat32List(getOffset(pointer), length);
  }

  public List<Float> getFloat32List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getFloat32List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, float value) {
    checkForConstantValue(pointer, 0, value);
    setUnchecked(SET_VALUE, pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, float... values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<Float> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, float value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, float... values) {
    checkArrayRange(pointer, index, index + values.length);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Float> values) {
    checkArrayRange(pointer, index, index + values.size());
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, (float) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, float value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getFloat32(getOffset(pointer, index));
      pointer.getByteArray().setFloat32(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, index, old, value);
    } else {
      pointer.getByteArray().setFloat32(getOffset(pointer, index), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, float value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, float... values) {
    //todo implement value change listener
    pointer.getByteArray().setFloat32(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, float[] values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Float> values) {
    //todo implement value change listener
    pointer.getByteArray().setFloat32(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Float> values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, float value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, float... values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Float> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, float value) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, float... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.length);
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Float> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.size());
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isArray()) {
      long length = getArrayLength(pointer);
      for (int i = 0; i < length; i++) {
        pointer.getByteArray().addFloat32(getOffset(pointer, i), constantValue != null ? constantValue : 0.0f);
      }
    } else {
      pointer.getByteArray().addFloat32(getOffset(pointer, 0), constantValue != null ? constantValue : 0.0f);
    }
  }

  @Override
  protected void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addFloat32(getOffset(pointer, index), constantValue != null ? constantValue : 0.0f);
      });
    });
  }

  @Override
  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var values = new float[Math.toIntExact(length)];
        for (int i = 0; i < length; i++) {
          values[i] = constantValue != null ? constantValue : 0.0f;
        }
        pointer.getByteArray().addFloat32(getOffset(pointer, index), values);
      });
    });
  }
}
