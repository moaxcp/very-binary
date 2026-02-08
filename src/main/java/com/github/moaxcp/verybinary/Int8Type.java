package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT8;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int8Type extends NumberType<Int8Type, Byte> {

  public static Int8Type int8() {
    return int8(-1);
  }

  public static Int8Type int8(int position) {
    return new Int8Type(position);
  }

  public Int8Type(int position, @Nullable Byte constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, INT8, constantValue, lengthExpression, byteLengthExpression);
  }

  public Int8Type(int position) {
    super(position, INT8);
  }

  @Override
  public Int8Type copy(int position) {
    return new Int8Type(position, constantValue, lengthExpression, byteLengthExpression);
  }

  public byte getInt8(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt8(getOffset(pointer));
  }

  public byte getInt8(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getInt8(getOffset(pointer, index));
  }

  public byte[] getInt8Array(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt8(getOffset(pointer), length);
  }

  public byte[] getInt8Array(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt8(getOffset(pointer, index), length);
  }

  public List<Byte> getInt8List(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    if (length == 0) {
      return List.of();
    }
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt8List(getOffset(pointer), length);
  }

  public List<Byte> getInt8List(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt8List(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, byte value) {
    checkForConstantValue(pointer, 0, value);
    setUnchecked(SET_VALUE, pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, byte... values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, List<Byte> values) {
    checkArrayRange(pointer, 0, values.size());
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, byte value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, byte... values) {
    checkArrayRange(pointer, index, index + values.length);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Byte> values) {
    checkArrayRange(pointer, index, index + values.size());
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, (byte) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, 0, (byte) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, byte value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getInt8(getOffset(pointer, index));
      if(value != old) {
        pointer.getByteArray().setInt8(getOffset(pointer, index), value);
        notifyValueChange(reason, pointer, index, old, value);
      }
    } else {
      pointer.getByteArray().setInt8(getOffset(pointer, index), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, byte value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, byte[] values) {
    //todo implement value change listener
    pointer.getByteArray().setInt8(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, byte[] values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Byte> values) {
    //todo implement value change listener
    pointer.getByteArray().setInt8(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Byte> values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, byte value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, byte... values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Byte> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, byte value) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, byte... values) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Byte> values) {
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
    if(isArray()) {
        long length = getArrayLength(pointer);
        for (long i = 0; i < length; i++) {
          pointer.getByteArray().addInt8(getOffset(pointer, i), constantValue != null ? constantValue : 0);
        }
    } else {
      pointer.getByteArray().addInt8(getOffset(pointer, 0), constantValue != null ? constantValue : 0);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addInt8(getOffset(pointer, index), constantValue != null ? constantValue : 0);
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var values = new byte[Math.toIntExact(length)];
        for (int i = 0; i < length; i++) {
          values[i] = constantValue != null ? constantValue : 0;
        }
        pointer.getByteArray().addInt8(getOffset(pointer, index), values);
      });
    });
  }
}
