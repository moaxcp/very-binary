package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public sealed class Constant implements Expression permits BasicElementLengthOf {

  private final long value;

  Constant(long value) {
    this.value = value;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return true;
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    return value;
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    return value;
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return value;
  }

  @Override
  public String toString() {
    return "Constant{" +
        "value=" + value +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    var constant = (Constant) o;
    return value == constant.value;
  }

  @Override
  public int hashCode() {
    return Long.hashCode(value);
  }
}
