package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;

public abstract sealed class ArithmeticValue extends Value<ArithmeticValue> implements ArithmeticExpression permits BoolValue, Float32Value, Float64Value, Int16Value, Int32Value, Int64Value, Int8Value, Uint16Value, Uint32Value, Uint64Value, Uint8Value {

  public static final Int32Value ZERO = int32Value(0);

  public abstract boolean toBool();

  public abstract byte toByte();

  public abstract short toShort();

  public abstract int toInt();

  public abstract long toLong();

  public abstract BigInteger toBigInteger();

  public abstract float toFloat();

  public abstract double toDouble();

  public abstract ArithmeticValue sum(ArithmeticValue value);

  public abstract ArithmeticValue multiply(ArithmeticValue value);

  public abstract ArithmeticValue subtract(ArithmeticValue value);

  public abstract ArithmeticValue divide(ArithmeticValue value);
}
