package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;

import static com.github.moaxcp.verybinary.math.BooleanGatherer.equality;
import static java.util.stream.Collectors.joining;

public final class SameExpression implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  public static SameExpression same(List<ArithmeticExpression> expressions) {
    return new SameExpression(expressions);
  }

  public SameExpression(List<ArithmeticExpression> expressions) {
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
    return expressions.stream().map(e -> e.constantValue(parent)).gather(equality()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return expressions.stream().map(e -> e.defaultValue(parent)).gather(equality()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return expressions.stream().map(e -> e.evaluate(pointer)).gather(equality()).findAny().orElse(BoolValue.FALSE);
  }

  @Override
  public String toString() {
    return "same(" + expressions.stream().map(Object::toString).collect(joining(", ")) + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    SameExpression that = (SameExpression) o;
    return expressions.equals(that.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }
}
