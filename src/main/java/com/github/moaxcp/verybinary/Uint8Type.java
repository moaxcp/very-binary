package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT8;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint8Type extends PrimitiveType<Uint8Type, Short> implements LengthType<Uint8Type, Short> {

  private final short constantValue;
  private final boolean constantValueSet;

  public Uint8Type(int position, @Nullable ComplexType<?> parent, @Nullable Short constantValue) {
    super(position, parent, UINT8);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint8Type copy(int position, @Nullable ComplexType<?> parent) {
    return new Uint8Type(position, parent, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public short getUint8ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public short getUint8(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint8(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, short value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (short) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (short) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, short value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getUint8(pointer);
      if (value != old) {
        pointer.getByteArray().setUint8(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint8(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint8(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
