package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT8;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int8Type extends NumberType<Int8Type, Byte> {

  private final byte constantValue;
  private final boolean constantValueSet;

  public Int8Type(int position, @Nullable Byte constantValue) {
    super(position, INT8);
    this.constantValue = constantValue != null ? constantValue : 0;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int8Type copy(int position) {
    return new Int8Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public byte getInt8ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValueSet ? (long) constantValue : 0;
  }

  public byte getInt8(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt8(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, byte value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (byte) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (byte) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, byte value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt8(getOffset(pointer));
      if(value != old) {
        pointer.getByteArray().setInt8(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt8(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, byte value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt8(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
