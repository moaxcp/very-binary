package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint32Type extends NumberType<Uint32Type, Long> {

  private final long constantValue;
  private final boolean constantValueSet;

  public Uint32Type(int position, @Nullable Long constantValue) {
    super(position, UINT32);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint32Type copy(int position) {
    return new Uint32Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValueSet ? constantValue : 0;
  }

  public long getUint32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint32(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint32(getOffset(pointer));
      if (value != old) {
        pointer.getByteArray().setUint32(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint32(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint32(getOffset(pointer), constantValueSet ? constantValue : 0L);
  }
}
