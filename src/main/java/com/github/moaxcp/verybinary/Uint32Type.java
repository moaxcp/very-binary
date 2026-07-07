package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint32Type extends PrimitiveType<Uint32Type, Long> implements LengthType<Uint32Type, Long> {

  private final long constantValue;
  private final boolean constantValueSet;

  public Uint32Type(int position, @Nullable ComplexType<?> parent, @Nullable Long constantValue) {
    super(position, parent, UINT32);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Uint32Type copy(int position, @Nullable ComplexType<?> parent) {
    return new Uint32Type(position, parent, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public long getUint32ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public long getUint32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint32(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getUint32(pointer);
      if (value != old) {
        pointer.getByteArray().setUint32(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint32(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint32(getOffset(pointer), constantValueSet ? constantValue : 0L);
  }
}
