package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.List;

public final class EqualityExpression implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  public EqualityExpression(List<ArithmeticExpression> expressions) {
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
    return null;
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return null;
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    return null;
  }
}
