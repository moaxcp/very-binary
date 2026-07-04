package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT8;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int8Type extends PrimitiveType<Int8Type, Byte> implements LengthType<Int8Type, Byte> {

  private final byte constantValue;
  private final boolean constantValueSet;

  public Int8Type(int position, @Nullable Byte constantValue, @Nullable ComplexType<?> parent) {
    super(position, INT8, parent);
    this.constantValue = constantValue != null ? constantValue : 0;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int8Type copy(int position, @Nullable ComplexType<?> parent) {
    return new Int8Type(position, constantValueSet ? constantValue : null, parent);
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
    return constantValue;
  }

  public byte getInt8(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt8(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, byte value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (byte) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (byte) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, byte value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getInt8(pointer);
      if(value != old) {
        pointer.getByteArray().setInt8(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt8(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt8(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
