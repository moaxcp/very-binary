package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;
import java.util.StringJoiner;

public final class Subtract implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  Subtract(ArithmeticExpression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public Subtract(List<ArithmeticExpression> expressions) {
    if (expressions == null || expressions.size() < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = expressions;
  }

  static Subtract subtract(ArithmeticExpression... expressions) {
    return new Subtract(expressions);
  }

  static ArithmeticExpression simplify(Subtract sub) {
    return sub;
  }

  public List<ArithmeticExpression> expressions() {
    return expressions;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return expressions.stream().allMatch(e -> e.isConstant(parent));
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    var result = (ArithmeticValue) expressions.get(0).constantValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.subtract((ArithmeticValue) expressions.get(i).constantValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    var result = (ArithmeticValue) expressions.get(0).defaultValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.subtract((ArithmeticValue) expressions.get(i).defaultValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    var result = (ArithmeticValue) expressions.get(0).evaluate(pointer);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.subtract((ArithmeticValue) expressions.get(i).evaluate(pointer));
    }
    return result;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" - ");
    for (Expression expression : expressions) {
      if (expression instanceof Multiply || expression instanceof Divide) {
        joiner.add("(" + expression + ")");
      } else {
        joiner.add(expression.toString());
      }
    }
    return joiner.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Subtract subtract = (Subtract) o;
    return expressions.equals(subtract.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.stream()
        .mapToInt(Object::hashCode)
        .sum();
  }
}
