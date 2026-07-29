package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public final class Sum implements MultiExpression {

  private final List<Expression> expressions;

  Sum(Expression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public Sum(List<Expression> expressions) {
    if (expressions.size() < 2) {
      throw new IllegalArgumentException("Sum must have at least two expressions");
    }
    this.expressions = expressions;
  }

  static Sum sum(Expression... expressions) {
    return new Sum(expressions);
  }

  static Sum sum(List<Expression> expressions) {
    return new Sum(expressions);
  }

  static Expression simplify(Sum sum) {
    return sum;
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
    StringJoiner joiner = new StringJoiner(" + ");
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
