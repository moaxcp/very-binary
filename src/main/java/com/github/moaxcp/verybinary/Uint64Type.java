package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Uint64Type extends BasicType<Uint64Type, BigInteger> implements LengthType<Uint64Type, BigInteger> {

  public Uint64Type(int position, @Nullable BigInteger constantValue, @Nullable ComplexType parent) {
    super(position, UINT64, constantValue, parent);
    this.constantValue = constantValue;
  }

  @Override
  public Uint64Type copy(int position, @Nullable ComplexType parent) {
    return new Uint64Type(position, constantValue, parent);
  }

  @Override
  public @Nullable BigInteger getConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return constantValue != null ? constantValue.longValue() : 0;
  }

  @Override
  public BigInteger get(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getUint64(getOffset(pointer));
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, BigInteger.valueOf(value));
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, BigInteger.valueOf(value));
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer);
      if (!value.equals(old)) {
        pointer.getByteArray().setUint64(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addUint64(getOffset(pointer), constantValue != null ? constantValue : BigInteger.ZERO);
  }
}
