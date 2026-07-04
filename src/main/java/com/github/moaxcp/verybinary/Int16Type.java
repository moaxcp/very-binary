package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT16;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int16Type extends PrimitiveType<Int16Type, Short> implements LengthType<Int16Type, Short> {

  private final short constantValue;
  private final boolean constantValueSet;

  public Int16Type(int position, @Nullable Short constantValue, @Nullable ComplexType<?> parent) {
    super(position, INT16, parent);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Int16Type copy(int position, @Nullable ComplexType<?> parent) {
    return new Int16Type(position, constantValueSet ? constantValue : null, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public short getInt16ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public short getInt16(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt16(getOffset(pointer));
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
      var old = getInt16(pointer);
      if (value != old) {
        pointer.getByteArray().setInt16(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt16(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt16(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
