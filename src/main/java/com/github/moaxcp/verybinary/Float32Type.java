package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.FLOAT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Float32Type extends NumberType<Float32Type, Float> {

  private final boolean constantValueSet;
  private final float constantValue;

  public Float32Type(int position, @Nullable Float constantValue) {
    super(position, FLOAT32);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Float32Type copy(int position) {
    return new Float32Type(position, constantValueSet ? constantValue : null);
  }

  @Override
  public long defaultArrayLengthValue() {
    return constantValueSet ? (long) constantValue : 0;
  }

  public float getFloat32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat32(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, float value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (float) value);
  }

  void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (float) value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, float value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getFloat32(getOffset(pointer));
      if (value != old) {
        pointer.getByteArray().setFloat32(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setFloat32(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, float value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addFloat32(getOffset(pointer), constantValueSet ? constantValue : 0);
  }
}
