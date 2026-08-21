package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public final class Sum implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  Sum(ArithmeticExpression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public Sum(List<ArithmeticExpression> expressions) {
    if (expressions.size() < 2) {
      throw new IllegalArgumentException("Sum must have at least two expressions");
    }
    this.expressions = expressions;
  }

  static Sum sum(ArithmeticExpression... expressions) {
    return new Sum(expressions);
  }

  static Sum sum(List<ArithmeticExpression> expressions) {
    return new Sum(expressions);
  }

  static ArithmeticExpression simplify(Sum sum) {
    return sum;
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
    return expressions.stream().map(e -> (ArithmeticValue) e.constantValue(parent)).reduce(ArithmeticValue::sum).orElse(ArithmeticValue.ZERO);
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return expressions.stream().map(e -> (ArithmeticValue) e.defaultValue(parent)).reduce(ArithmeticValue::sum).orElse(ArithmeticValue.ZERO);
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return expressions.stream().map(e -> (ArithmeticValue) e.evaluate(pointer)).reduce(ArithmeticValue::sum).orElse(ArithmeticValue.ZERO);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" + ");
    for (var expression : expressions) {
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

    var other = ((Sum) o).expressions();
    if (expressions == other) return true;
    if (expressions.size() != other.size()) return false;
    Map<Expression, Long> map1 = expressions.stream().collect(groupingBy(e -> e, counting()));
    Map<Expression, Long> map2 = other.stream().collect(groupingBy(e -> e, counting()));

    return map1.equals(map2);
  }

  @Override
  public int hashCode() {
    return expressions.stream()
        .mapToInt(Object::hashCode)
        .sum();
  }
}
