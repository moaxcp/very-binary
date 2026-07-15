package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

public final class BasicElementLengthOf extends Constant {

  private final int position;

  BasicElementLengthOf(int position) {
    super(-1);
    this.position = position;
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return parent.getType(position) instanceof BasicListType<?, ?, ?>;
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    return ((BasicListType<?, ?, ?>) parent.getType(position)).getBasicTypeInfo().size();
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    return ((BasicListType<?, ?, ?>) parent.getType(position)).getBasicTypeInfo().size();
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return ((BasicListType<?, ?, ?>) ((ComplexPointer<?, ?>) pointer).getType(position)).getBasicTypeInfo().size();
  }

  @Override
  public String toString() {
    return "BasicElementLengthOf{" +
        "position=" + position +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    var variable = (BasicElementLengthOf) o;
    return position == variable.position();
  }

  @Override
  public int hashCode() {
    return position;
  }
}
