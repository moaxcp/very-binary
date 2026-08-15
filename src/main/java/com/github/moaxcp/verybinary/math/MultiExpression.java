package com.github.moaxcp.verybinary.math;

import java.util.List;

public sealed interface MultiExpression<T extends Expression<V>, V extends Value<V>> extends Expression<V> permits Divide, GreaterThanExpression, GreaterThanOrEqualExpression, LessThanExpression, LessThanOrEqualExpression, Multiply, NotSameExpression, SameExpression, Subtract, Sum {
  List<T> expressions();
}
