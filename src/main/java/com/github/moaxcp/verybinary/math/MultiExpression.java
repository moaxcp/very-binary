package com.github.moaxcp.verybinary.math;

import java.util.List;

public sealed interface MultiExpression<T extends Expression<V>, V extends Value<V>> extends Expression<V> permits Divide, EqualityExpression, Multiply, Subtract, Sum {
  List<T> expressions();
}
