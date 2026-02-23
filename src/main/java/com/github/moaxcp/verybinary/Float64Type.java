package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.FLOAT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Float64Type extends NumberType<Float64Type, Double> {
  private boolean constantValueSet;
  private double constantValue;

  public Float64Type(int position, @Nullable Double constantValue) {
    super(position, FLOAT64);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0.0d;
  }

  @Override
  public Float64Type copy(int position) {
    return new Float64Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValueSet ? (long) constantValue : 0L;
  }

  public double getFloat64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat64(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, double value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (double) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (double) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, double value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getFloat64(getOffset(pointer));
      pointer.getByteArray().setFloat64(getOffset(pointer), value);
      notifyValueChange(reason, pointer, old, value);
    }
    pointer.getByteArray().setFloat64(getOffset(pointer), value);
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, double value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addFloat64(getOffset(pointer), constantValueSet ? constantValue : 0.0d);
  }
}
