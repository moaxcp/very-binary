package com.github.moaxcp.verybinary.math;

import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;

public abstract sealed class ArithmeticValue extends Value permits Float32Value, Float64Value, Int16Value, Int32Value, Int64Value, Int8Value, Uint16Value, Uint32Value, Uint64Value, Uint8Value {

  public static final ArithmeticValue ZERO = int32Value(0);

  public abstract ArithmeticValue sum(ArithmeticValue value);

  public abstract ArithmeticValue multiply(ArithmeticValue value);

  public abstract ArithmeticValue subtract(ArithmeticValue value);

  public abstract ArithmeticValue divide(ArithmeticValue value);
}
