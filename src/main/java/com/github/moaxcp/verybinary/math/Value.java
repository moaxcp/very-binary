package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public abstract sealed class Value implements Expression permits ArithmeticValue, BoolValue {
  public Value evaluate(Pointer<?, ? extends Type<?>> pointer) {
    throw new UnsupportedOperationException("evaluation of Value not allowed");
  }

  public final boolean isConstant(ComplexType<?> parent) {
    return true;
  }

  public final Value constantValue(ComplexType<?> parent) {
    return this;
  }

  public final Value defaultValue(ComplexType<?> parent) {
    return this;
  }
}
