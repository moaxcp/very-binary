package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

import static com.github.moaxcp.verybinary.Primitive.BOOL;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

/**
 * Boolean type backed by a single byte in ByteArray.
 * Primitive boolean API only; wrapper-based Type methods throw just like NumberType primitives do.
 */
public final class BoolType extends PrimitiveType<BoolType, Boolean> {

  private final boolean constantValueSet;
  private final boolean constantValue;

  public BoolType(int position, @Nullable Boolean constantValue) {
    super(position, BOOL);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : false;
  }

  @Override
  public BoolType copy(int position) {
    return new BoolType(position, constantValueSet ? constantValue : null);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public boolean getBool(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getBool(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    checkForConstantValue(pointer, value);
    setUnchecked(SET_VALUE, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, boolean value) {
    if (!getValueChangeListeners().isEmpty()) {
      var old = pointer.getByteArray().getBool(getOffset(pointer));
      pointer.getByteArray().setBool(getOffset(pointer), value);
      notifyValueChange(reason, pointer, old, value);
    } else {
      pointer.getByteArray().setBool(getOffset(pointer), value);
    }
  }

  private void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    if (isConstant() && !Objects.equals(constantValue, value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + constantValue);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addBool(getOffset(pointer), constantValueSet && constantValue);
  }
}
