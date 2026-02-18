package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int64Type extends NumberType<Int64Type, Long> {

  private final long constantValue;
  private final boolean constantValueSet;

  public Int64Type(int position, @Nullable Long constantValue) {
    super(position, INT64);
    this.constantValue = constantValue != null ? constantValue : 0L;
    this.constantValueSet = constantValue != null;
  }

  @Override
  public Int64Type copy(int position) {
    return new Int64Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValueSet ? constantValue : 0;
  }

  public long getInt64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt64(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (long) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getInt64(getOffset(pointer));
      if (value != old) {
        pointer.getByteArray().setInt64(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setInt64(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt64(getOffset(pointer), constantValueSet ? constantValue : 0L);
  }
}
