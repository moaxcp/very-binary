package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.BasicTypeInfo.BOOL;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

/**
 * Boolean type backed by a single byte in ByteArray.
 * Primitive boolean API only; wrapper-based Type methods throw just like NumberType primitives do.
 */
public final class BoolType extends PrimitiveType<BoolType, Boolean> {

  private final boolean constantValueSet;
  private final boolean constantValue;

  public BoolType(int position, @Nullable Boolean constantValue, @Nullable ComplexType parent) {
    super(position, BOOL, parent);
    this.constantValueSet = constantValue != null;
    this.constantValue = constantValueSet ? constantValue : false;
  }

  @Override
  public BoolType copy(int position, @Nullable ComplexType parent) {
    return new BoolType(position, constantValueSet ? constantValue : null, parent);
  }

  @Override
  public boolean isConstant() {
    return constantValueSet;
  }

  public boolean getBoolConstantValue() {
    return constantValue;
  }

  public boolean getBool(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getBool(getOffset(pointer));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, boolean value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, boolean value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = getBool(pointer);
      if (value != old) {
        pointer.getByteArray().setBool(getOffset(pointer), value);
        notifyValueChange(reason, pointer, old, value);
      }
    } else {
      pointer.getByteArray().setBool(getOffset(pointer), value);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    pointer.getByteArray().addBool(getOffset(pointer), constantValueSet && constantValue);
  }
}
