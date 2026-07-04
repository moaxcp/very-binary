package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int32Type extends PrimitiveType<Int32Type, Integer> implements LengthType<Int32Type, Integer> {
  private final int constantValue;
  private final boolean constantValueSet;

  public Int32Type(int position, @Nullable Integer constantValue, @Nullable ComplexType<?> parent) {
    super(position, INT32, parent);
    this.constantValue = constantValue != null ? constantValue : 0;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int32Type copy(int position, @Nullable ComplexType<?> parent) {
    return new Int32Type(position, constantValueSet ? constantValue : null, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public int getInt32ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public int getInt32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt32(getOffset(pointer));
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
      var old = getInt32(pointer);
      if (value != old) {
        pointer.getByteArray().setInt32(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt32(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt32(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
