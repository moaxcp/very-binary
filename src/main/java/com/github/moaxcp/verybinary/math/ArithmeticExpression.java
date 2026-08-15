package com.github.moaxcp.verybinary.math;

public sealed interface ArithmeticExpression extends Expression<ArithmeticValue> permits ArithmeticValue, ByteLengthOf, ByteLengthOfBasicElement, Divide, GreaterThanExpression, GreaterThanOrEqualExpression, LengthOf, LessThanExpression, LessThanOrEqualExpression, Multiply, NotSameExpression, SameExpression, Subtract, Sum, Variable {

}
