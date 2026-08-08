package com.github.moaxcp.verybinary.math;

public sealed interface ArithmeticExpression extends Expression<ArithmeticValue> permits ArithmeticValue, ByteLengthOf, ByteLengthOfBasicElement, Divide, EqualityExpression, LengthOf, Multiply, Subtract, Sum, Variable {

}
