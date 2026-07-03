package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT16;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint16Type extends PrimitiveType<Uint16Type, Integer> implements LengthType<Uint16Type, Integer> {

  private final int constantValue;
  private final boolean constantValueSet;

  public Uint16Type(int position, @Nullable Integer constantValue, @Nullable ComplexType parent) {
    super(position, UINT16, parent);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint16Type copy(int position, @Nullable ComplexType parent) {
    return new Uint16Type(position, constantValue, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public int getUint16ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public int getUint16(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint16(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, int value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (int) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (int) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, int value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getUint16(pointer);
      if (value != old) {
        pointer.getByteArray().setUint16(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint16(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint16(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
