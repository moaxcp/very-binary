package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public abstract sealed class Value<SELF extends Value<SELF>> implements Expression<SELF> permits ArithmeticValue, StructValue {
  public final SELF evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return (SELF) this;
  }
  public final boolean isConstant(ComplexType<?> parent) {
    return true;
  }

  public final SELF constantValue(ComplexType<?> parent) {
    return (SELF) this;
  }

  public final SELF defaultValue(ComplexType<?> parent) {
    return (SELF) this;
  }
}
