package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint64Type extends NumberType<Uint64Type, BigInteger> {

  public static Uint64Type uint64Type() {
    return uint64Type(-1);
  }

  public static Uint64Type uint64Type(int position) {
    return new Uint64Type(position);
  }

  public Uint64Type(int position, @Nullable BigInteger constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, UINT64, constantValue, lengthExpression, byteLengthExpression);
  }

  public Uint64Type(int position) {
    super(position, UINT64);
  }

  @Override
  public Uint64Type copy(int position) {
    return new Uint64Type(position, constantValue, lengthExpression, byteLengthExpression);
  }

  public BigInteger getUint64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint64(getOffset(pointer));
  }

  public BigInteger getUint64(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getUint64(getOffset(pointer, index));
  }

  public List<BigInteger> getUint64List(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getUint64List(getOffset(pointer), length);
  }

  public List<BigInteger> getUint64List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint64List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    checkForConstantValue(pointer, 0, value);
    setUnchecked(SET_VALUE, pointer, 0, value);
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

  protected void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, BigInteger.valueOf(value));
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, 0, BigInteger.valueOf(value));
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getUint64(getOffset(pointer, index));
      if (!value.equals(old)) {
        pointer.getByteArray().setUint64(getOffset(pointer, index), value);
        notifyValueChange(reason, pointer, index, old, value);
      }
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    //todo implement value change listener
    pointer.getByteArray().setUint64(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<BigInteger> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
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
        pointer.getByteArray().addUint64(getOffset(pointer, i), constantValue != null ? constantValue : BigInteger.ZERO);
      }
    } else {
      pointer.getByteArray().addUint64(getOffset(pointer, 0), constantValue != null ? constantValue : BigInteger.ZERO);
    }
  }

  @Override
  protected void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addUint64(getOffset(pointer, index), constantValue != null ? constantValue : BigInteger.ZERO);
      });
    });
  }

  @Override
  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var values = new BigInteger[(int) length];
        for (int i = 0; i < length; i++) {
          values[i] = constantValue != null ? constantValue : BigInteger.ZERO;
        }
        pointer.getByteArray().addUint64(getOffset(pointer, index), values);
      });
    });
  }
}
