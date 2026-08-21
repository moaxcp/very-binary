package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;
import java.util.StringJoiner;

import static com.github.moaxcp.verybinary.math.BooleanGatherer.lessThan;

public final class LessThanExpression implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  public LessThanExpression(List<ArithmeticExpression> expressions) {
    this.expressions = List.copyOf(expressions);
  }

  @Override
  public List<ArithmeticExpression> expressions() {
    return expressions;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return expressions.stream().allMatch(e -> e.isConstant(parent));
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    return expressions.stream().map(e -> e.constantValue(parent)).gather(lessThan()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return expressions.stream().map(e -> e.defaultValue(parent)).gather(lessThan()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return expressions.stream().map(e -> e.evaluate(pointer)).gather(lessThan()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" < ");
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

    LessThanExpression that = (LessThanExpression) o;
    return expressions.equals(that.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }
}
