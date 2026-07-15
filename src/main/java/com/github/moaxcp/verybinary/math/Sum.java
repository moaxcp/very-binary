package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;

public final class Sum implements Expression {

  private final List<Expression> expressions;

  Sum(Expression... expressions) {
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
    return expressions.stream().mapToLong(e -> e.constantValue(parent)).sum();
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    return expressions.stream().mapToLong(e -> e.defaultValue(parent)).sum();
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return expressions.stream().mapToLong(e -> e.evaluate(pointer)).sum();
  }

  @Override
  public String toString() {
    return "Sum{" +
        "expressions=" + expressions +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Sum subtract = (Sum) o;
    return expressions.equals(subtract.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }
}
