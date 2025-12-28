package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ArrayLengthListener.ArrayLengthReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.BOOL;

/**
 * Boolean type backed by a single byte in ByteArray.
 * Primitive boolean API only; wrapper-based Type methods throw just like NumberType primitives do.
 */
public final class BoolType extends PrimitiveType<BoolType, Boolean> {

  public static BoolType bool() {
    return bool(-1);
  }

  public static BoolType bool(int position) {
    return new BoolType(position);
  }

  public BoolType(int position, @Nullable Boolean constantValue, @Nullable Expression lengthExpression) {
    super(position, BOOL, constantValue, lengthExpression);
  }

  public BoolType(int position) {
    super(position, BOOL);
  }

  @Override
  protected BoolType copy(int position) {
    return new BoolType(position, constantValue, lengthExpression);
  }

  public boolean getBoolean(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getBool(getOffset(pointer));
  }

  public boolean getBoolean(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getBool(getOffset(pointer, index));
  }

  public boolean[] getBooleanArray(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getBool(getOffset(pointer), length);
  }

  public boolean[] getBooleanArray(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkIndex(pointer, index + length);
    return pointer.getByteArray().getBool(getOffset(pointer, index), length);
  }

  public List<Boolean> getBooleanList(Pointer<?, ? extends Type<?>> pointer) {
    var length = getArrayLength(pointer);
    checkIndex(pointer, length);
    return pointer.getByteArray().getBoolList(getOffset(pointer, 0), length);
  }

  public List<Boolean> getBooleanList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkIndex(pointer, index + length);
    return pointer.getByteArray().getBoolList(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    setUnchecked(pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean... values) {
    checkIndex(pointer, values.length - 1);
    setUnchecked(pointer, 0, values);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, List<Boolean> values) {
    setUnchecked(pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    checkIndex(pointer, index);
    setUnchecked(pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    checkIndex(pointer, index + values.length);
    setUnchecked(pointer, index, values);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    checkIndex(pointer, index + values.size());
    setUnchecked(pointer, index, values);
  }

  private void setUnchecked(Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    if (isConstant(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new UnsupportedOperationException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
    pointer.getByteArray().setBool(getOffset(pointer, index), value);
  }

  private void setUnchecked(Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    for (var value : values) {
      if (isConstant(pointer.getType()) && !Objects.equals(constantValue, value)) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
      }
    }
    pointer.getByteArray().setBool(getOffset(pointer, index), values);
  }

  private void setUnchecked(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    for (var value : values) {
      if (isConstant(pointer.getType()) && !Objects.equals(constantValue, value)) {
        throw new UnsupportedOperationException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
      }
    }
    pointer.getByteArray().setBool(getOffset(pointer, index), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, boolean... values) {
    add(pointer, getArrayLength(pointer), values);
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, List<Boolean> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + 1);
    }
    allocate(pointer, index);
    setUnchecked(pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.length);
    }
    allocate(pointer, index, values.length);
    setUnchecked(pointer, index, values);
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.size());
    }
    allocate(pointer, index, values.size());
    setUnchecked(pointer, index, values);
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isArray()) {
      long length = getArrayLength(pointer);
      for (int i = 0; i < length; i++) {
        pointer.getByteArray().addBool(getOffset(pointer, i), constantValue != null ? constantValue : false);
      }
    } else {
      pointer.getByteArray().addBool(getOffset(pointer, 0), constantValue != null ? constantValue : false);
    }
  }

  @Override
  protected void allocate(ArrayLengthReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addBool(getOffset(pointer, index), constantValue != null ? constantValue : false);
      });
    });
  }

  @Override
  void allocate(ArrayLengthReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(pointer, () -> {
        checkIndexAllocate(pointer, index);
        var values = new boolean[Math.toIntExact(length)];
        for (int i = 0; i < length; i++) {
          values[i] = constantValue != null ? constantValue : false;
        }
        pointer.getByteArray().addBool(getOffset(pointer, index), values);
      });
    });
  }
}
