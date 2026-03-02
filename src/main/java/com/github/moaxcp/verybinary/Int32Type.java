package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int32Type extends NumberType<Int32Type, Integer> {
  private final int constantValue;
  private final boolean constantValueSet;

  public Int32Type(int position, @Nullable Integer constantValue) {
    super(position, INT32);
    this.constantValue = constantValue != null ? constantValue : 0;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int32Type copy(int position) {
    return new Int32Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public int getInt32ConstantValue() {
    return constantValueSet ? constantValue : 0;
  }

  @Override
  public long defaultLengthValue() {
    return constantValueSet ? constantValue : 0;
  }

  public int getInt32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt32(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, int value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (int) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (int) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, int value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt32(getOffset(pointer));
      pointer.getByteArray().setInt32(getOffset(pointer), value);
      notifyValueChange(reason, pointer, old, value);
    } else {
      pointer.getByteArray().setInt32(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, int value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt32(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
