package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;

public final class Subtract implements Expression {

  private final List<Expression> expressions;

  Subtract(Expression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public List<Expression> expressions() {
    return expressions;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return expressions.stream().allMatch(e -> e.isConstant(parent));
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    long result = expressions.get(0).constantValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result -= expressions.get(i).constantValue(parent);
    }
    return result;
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    long result = expressions.get(0).defaultValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result -= expressions.get(i).defaultValue(parent);
    }
    return result;
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    long result = expressions.get(0).evaluate(pointer);
    for (int i = 1; i < expressions.size(); i++) {
      result -= expressions.get(i).evaluate(pointer);
    }
    return result;
  }

  @Override
  public String toString() {
    return "Subtract{" +
        "expressions=" + expressions +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Subtract subtract = (Subtract) o;
    return expressions.equals(subtract.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }
}
