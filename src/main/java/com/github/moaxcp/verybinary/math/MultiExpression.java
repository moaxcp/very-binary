package com.github.moaxcp.verybinary.math;

import java.util.List;

public sealed interface MultiExpression extends Expression permits Divide, Multiply, Subtract, Sum {
  List<Expression> expressions();
}
