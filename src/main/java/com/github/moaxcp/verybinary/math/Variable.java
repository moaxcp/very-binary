package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public final class Variable implements ArithmeticExpression {

  private final String name;
  @Nullable
  private final ArithmeticValue value;

  public Variable(String name, @Nullable ArithmeticValue value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    if (value == null) {
      throw new IllegalArgumentException("Variable must have a value");
    }
    return true;
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    if (value == null) {
      throw new IllegalArgumentException("Variable must have a value");
    }
    return value;
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    if (value == null) {
      throw new IllegalArgumentException("Variable must have a value");
    }
    return value;
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    if (value == null) {
      throw new IllegalArgumentException("Variable must have a value");
    }
    return value;
  }

  @Override
  public String toString() {
    return value != null ? value.toString() : name;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Variable variable = (Variable) o;
    return name.equals(variable.name) && Objects.equals(value, variable.value);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + Objects.hashCode(value);
    return result;
  }
}
