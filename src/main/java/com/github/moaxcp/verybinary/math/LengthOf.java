package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;

public final class LengthOf implements ArithmeticExpression {

  private final int position;

  LengthOf(int position) {
    this.position = position;
  }

  public static LengthOf lengthOf(int position) {
    return new LengthOf(position);
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    var type = ((ListType<?, ?, ?>) parent.getType(position));
    return type.isFixedByteLength();
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    var type = (ListType<?, ?, ?>) parent.getType(position);
    return int64Value(type.getAllocationLength());
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    var type = (ListType<?, ?, ?>) parent.getType(position);
    return int64Value(type.getAllocationLength());
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return int64Value(((ListType<?, ?, ?>)((ComplexPointer<?, ?>) pointer).getType(position)).getLength(pointer));
  }

  @Override
  public String toString() {
    return "LengthOf(" +
        position +
        ')';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    LengthOf variable = (LengthOf) o;
    return position == variable.position();
  }

  @Override
  public int hashCode() {
    return position;
  }
}
