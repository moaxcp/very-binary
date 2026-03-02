package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.INT16;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Int16Type extends NumberType<Int16Type, Short> {

  private final short constantValue;
  private final boolean constantValueSet;

  public Int16Type(int position, @Nullable Short constantValue) {
    super(position, INT16);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Int16Type copy(int position) {
    return new Int16Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public short getInt16ConstantValue() {
    return constantValueSet ? constantValue : 0;
  }

  @Override
  public long defaultLengthValue() {
    return constantValueSet ? (long) constantValue : 0;
  }

  public short getInt16(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt16(getOffset(pointer));
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
      var old = pointer.getByteArray().getInt16(getOffset(pointer));
      pointer.getByteArray().setInt16(getOffset(pointer), value);
      notifyValueChange(reason, pointer, old, value);
    } else {
      pointer.getByteArray().setInt16(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, short value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addInt16(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
