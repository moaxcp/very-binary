package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public final class StructVariable implements Expression<StructValue> {
  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return false;
  }

  @Override
  public StructValue constantValue(ComplexType<?> parent) {
    return null;
  }

  @Override
  public StructValue defaultValue(ComplexType<?> parent) {
    return null;
  }

  @Override
  public StructValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return null;
  }
}
