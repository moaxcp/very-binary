package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

public final class ByteLengthOf implements Expression {

  private final int position;

  ByteLengthOf(int position) {
    this.position = position;
  }

  public static ByteLengthOf byteLengthOf(int position) {
    return new ByteLengthOf(position);
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    var type = ((ListType<?, ?, ?>) parent.getType(position));
    return type.isFixedByteLength() && type.isElementFixedLength();
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    var type = (ListType<?, ?, ?>) parent.getType(position);
    return type.getAllocationByteLength();
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    var type = (ListType<?, ?, ?>) parent.getType(position);
    return type.getAllocationByteLength();
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return ((ListType<?, ?, ?>)((ComplexPointer<?, ?>) pointer).getType(position)).getByteLength(pointer);
  }

  @Override
  public String toString() {
    return "ByteLengthOf(" +
        position +
        ')';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    ByteLengthOf variable = (ByteLengthOf) o;
    return position == variable.position();
  }

  @Override
  public int hashCode() {
    return position;
  }
}
