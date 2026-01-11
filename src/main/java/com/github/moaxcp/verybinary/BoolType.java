package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.BOOL;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

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

  public BoolType(int position, @Nullable Boolean constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position, BOOL, constantValue, lengthExpression, byteLengthExpression);
  }

  public BoolType(int position) {
    super(position, BOOL);
  }

  @Override
  public BoolType copy(int position) {
    return new BoolType(position, constantValue, lengthExpression, byteLengthExpression);
  }

  public boolean getBool(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getBool(getOffset(pointer));
  }

  public boolean getBool(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getBool(getOffset(pointer, index));
  }

  public boolean[] getBoolArray(Pointer<?, ? extends Type<?>> pointer) {
    long length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getBool(getOffset(pointer), length);
  }

  public boolean[] getBoolArray(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getBool(getOffset(pointer, index), length);
  }

  public List<Boolean> getBoolList(Pointer<?, ? extends Type<?>> pointer) {
    var length = getArrayLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getBoolList(getOffset(pointer, 0), length);
  }

  public List<Boolean> getBoolList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getBoolList(getOffset(pointer, index), length);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    checkForConstantValue(pointer, 0, value);
    setUnchecked(SET_VALUE, pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean... values) {
    checkArrayRange(pointer, 0, values.length);
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, List<Boolean> values) {
    checkForConstantValues(pointer, 0, values);
    setUnchecked(SET_VALUE, pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    checkIndex(pointer, index);
    checkForConstantValue(pointer, index, value);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    checkArrayRange(pointer, index, index + values.length);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    checkArrayRange(pointer, index, index + values.size() - 1);
    checkForConstantValues(pointer, index, values);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getBool(getOffset(pointer, index));
      pointer.getByteArray().setBool(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, index, old, value);
    } else {
      pointer.getByteArray().setBool(getOffset(pointer, index), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, boolean value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    pointer.getByteArray().setBool(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, boolean[] values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    pointer.getByteArray().setBool(getOffset(pointer, index), values);
  }

  private void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    if (isConstantValue(pointer.getType())) {
      for (var value : values) {
        if (!Objects.equals(constantValue, value)) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
        }
      }
    }
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
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, boolean... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.length);
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException("Cannot add elements to fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Boolean> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + values.size());
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException("Cannot add elements to fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    checkForConstantValues(pointer, index, values);
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
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
  protected void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addBool(getOffset(pointer, index), constantValue != null ? constantValue : false);
      });
    });
  }

  @Override
  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
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
