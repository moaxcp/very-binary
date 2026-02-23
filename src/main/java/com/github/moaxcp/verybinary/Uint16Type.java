package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT16;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint16Type extends NumberType<Uint16Type, Integer> {

  private final int constantValue;
  private final boolean constantValueSet;

  public Uint16Type(int position, @Nullable Integer constantValue) {
    super(position, UINT16);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint16Type copy(int position) {
    return new Uint16Type(position, constantValue);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValueSet ? constantValue : 0;
  }

  public int getUint16(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint16(getOffset(pointer));
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
      var old = pointer.getByteArray().getUint16(getOffset(pointer));
      pointer.getByteArray().setUint16(getOffset(pointer), value);
      notifyValueChange(reason, pointer, old, value);
    } else {
      pointer.getByteArray().setUint16(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, int value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint16(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
