package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;

public final class ByteLengthOfBasicElement implements Expression {

  private final int position;

  ByteLengthOfBasicElement(int position) {
    this.position = position;
  }

  public static ByteLengthOfBasicElement lengthOfBasicElement(int position) {
    return new ByteLengthOfBasicElement(position);
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return parent.getType(position) instanceof BasicListType<?, ?, ?>;
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    return int64Value(((BasicListType<?, ?, ?>) parent.getType(position)).getBasicTypeInfo().size());
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return int64Value(((BasicListType<?, ?, ?>) parent.getType(position)).getBasicTypeInfo().size());
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return int64Value(((BasicListType<?, ?, ?>) ((ComplexPointer<?, ?>) pointer).getType(position)).getBasicTypeInfo().size());
  }

  @Override
  public String toString() {
    return "BasicElementLengthOf(" +
        position +
        ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    var variable = (ByteLengthOfBasicElement) o;
    return position == variable.position();
  }

  @Override
  public int hashCode() {
    return position;
  }
}
