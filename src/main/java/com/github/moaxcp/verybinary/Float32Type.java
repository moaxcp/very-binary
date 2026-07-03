package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.FLOAT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.*;

public final class Float32Type extends PrimitiveType<Float32Type, Float> implements LengthType<Float32Type, Float> {

  private final boolean constantValueSet;
  private final float constantValue;

  public Float32Type(int position, @Nullable Float constantValue, @Nullable ComplexType parent) {
    super(position, FLOAT32, parent);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : 0;
  }

  @Override
  public Float32Type copy(int position, @Nullable ComplexType parent) {
    return new Float32Type(position, constantValueSet ? constantValue : null, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public float getFloat32ConstantValue() {
    return constantValue;
  }

  @Override
  public long defaultLengthValue() {
    return (long) constantValue;
  }

  public float getFloat32(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getFloat32(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, float value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, float value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getFloat32(pointer);
      if (value != old) {
        pointer.getByteArray().setFloat32(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setFloat32(getOffset(pointer), value);
    }
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addFloat32(getOffset(pointer), constantValueSet ? constantValue : 0);
  }

  @Override
  public void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, (float) value);
  }

  @Override
  public void setForByteLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_BYTE_LENGTH, pointer, (float) value);
  }
}
