package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint64Type extends NumberType<Uint64Type, BigInteger> {

  private final @Nullable BigInteger constantValue;

  public Uint64Type(int position, @Nullable BigInteger constantValue) {
    super(position, UINT64);
    this.constantValue = constantValue;
  }

  @Override
  public Uint64Type copy(int position) {
    return new Uint64Type(position, constantValue);
  }

  @Override
  public @Nullable BigInteger getConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValue != null ? constantValue.longValue() : 0;
  }

  public BigInteger getUint64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint64(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, BigInteger.valueOf(value));
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, BigInteger.valueOf(value));
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getUint64(getOffset(pointer));
      if (!value.equals(old)) {
        pointer.getByteArray().setUint64(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer), value);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint64(getOffset(pointer), constantValue != null ? constantValue : BigInteger.ZERO);
  }
}
