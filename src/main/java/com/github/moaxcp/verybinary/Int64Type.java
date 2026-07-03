package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int64Type extends PrimitiveType<Int64Type, Long> implements LengthType<Int64Type, Long> {

  private final long constantValue;
  private final boolean constantValueSet;

  public Int64Type(int position, @Nullable Long constantValue, @Nullable ComplexType parent) {
    super(position, INT64, parent);
    this.constantValue = constantValue != null ? constantValue : 0L;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int64Type copy(int position, @Nullable ComplexType parent) {
    return new Int64Type(position, constantValueSet ? constantValue : null, parent);
  }

  public long getInt64ConstantValue() {
    return constantValue;
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue;
  }

  public long getInt64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt64(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (long) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getInt64(pointer);
      if (value != old) {
        pointer.getByteArray().setInt64(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt64(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt64(getOffset(pointer), constantValueSet ? constantValue : 0L);
  }
}
