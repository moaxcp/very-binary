package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.FLOAT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Float64Type extends PrimitiveType<Float64Type, Double> implements LengthType<Float64Type, Double> {
  private final boolean constantValueSet;
  private final double constantValue;

  public Float64Type(int position, @Nullable Double constantValue, @Nullable ComplexType parent) {
    super(position, FLOAT64, parent);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0.0d;
  }

  @Override
  public Float64Type copy(int position, @Nullable ComplexType parent) {
    return new Float64Type(position, constantValueSet ? constantValue : null, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public double getFloat64ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return (long) constantValue;
  }

  public double getFloat64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat64(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, double value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (double) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (double) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, double value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getFloat64(pointer);
      if (value != old) {
        pointer.getByteArray().setFloat64(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setFloat64(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addFloat64(getOffset(pointer), constantValueSet ? constantValue : 0.0d);
  }
}
