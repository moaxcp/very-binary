package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT8;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint8Type extends NumberType<Uint8Type, Short> {

  private final short constantValue;
  private final boolean constantValueSet;

  public Uint8Type(int position, @Nullable Short constantValue) {
    super(position, UINT8);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint8Type copy(int position) {
    return new Uint8Type(position, constantValueSet ? constantValue : null);
  }

  public short getUint8ConstantValue() {
    return constantValueSet ? constantValue : 0;
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  @Override
  public long defaultLengthValue() {
    return constantValueSet ? constantValue : 0;
  }

  public short getUint8(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint8(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, short value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (short) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (short) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, short value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint8(getOffset(pointer));
      if (value != old) {
        pointer.getByteArray().setUint8(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint8(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, short value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint8(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
