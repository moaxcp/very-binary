package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

public final class LengthOf implements Expression {

  private final int position;

  LengthOf(int position) {
    this.position = position;
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return parent.getType(position) instanceof ListType<?, ?, ?>;
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    return parent.getType(position) instanceof ListType<?, ?, ?> l && l.isFixedLength() ? l.getAllocationLength() : -1;
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    return parent.getType(position).getAllocationLength();
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return ((ComplexPointer<?, ?>) pointer).getType(position).getAllocationLength();
  }

  @Override
  public String toString() {
    return "LengthOf{" +
        "position=" + position +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Variable variable = (Variable) o;
    return position == variable.position();
  }

  @Override
  public int hashCode() {
    return position;
  }
}
