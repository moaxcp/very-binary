package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

public final class Float64Value extends ArithmeticValue {
  final double value;

  private Float64Value(double value) {
    this.value = value;
  }

  public static Float64Value float64Value(double value) {
    return new Float64Value(value);
  }

  @Override
  public boolean toBool() {
    return value != 0;
  }

  @Override
  public byte toByte() {
    return (byte) value;
  }

  @Override
  public short toShort() {
    return (short) value;
  }

  @Override
  public int toInt() {
    return (int) value;
  }

  @Override
  public long toLong() {
    return (long) value;
  }

  @Override
  public BigInteger toBigInteger() {
    return BigInteger.valueOf((long) value);
  }

  @Override
  public float toFloat() {
    return (float) value;
  }

  @Override
  public double toDouble() {
    return value;
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float64Value(value + v.toDouble());
      case Float32Value v -> float64Value(value + v.value);
      case Float64Value v -> float64Value(value + v.value);
      case Int8Value v -> float64Value(value + v.value);
      case Int16Value v -> float64Value(value + v.value);
      case Int32Value v -> float64Value(value + v.value);
      case Int64Value v -> float64Value(value + v.value);
      case Uint8Value v -> float64Value(value + v.value);
      case Uint16Value v -> float64Value(value + v.value);
      case Uint32Value v -> float64Value(value + v.value);
      case Uint64Value v -> float64Value(value + v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float64Value(value - v.toDouble());
      case Float32Value v -> float64Value(value - v.value);
      case Float64Value v -> float64Value(value - v.value);
      case Int8Value v -> float64Value(value - v.value);
      case Int16Value v -> float64Value(value - v.value);
      case Int32Value v -> float64Value(value - v.value);
      case Int64Value v -> float64Value(value - v.value);
      case Uint8Value v -> float64Value(value - v.value);
      case Uint16Value v -> float64Value(value - v.value);
      case Uint32Value v -> float64Value(value - v.value);
      case Uint64Value v -> float64Value(value - v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float64Value(value * v.toDouble());
      case Float32Value v -> float64Value(value * v.value);
      case Float64Value v -> float64Value(value * v.value);
      case Int8Value v -> float64Value(value * v.value);
      case Int16Value v -> float64Value(value * v.value);
      case Int32Value v -> float64Value(value * v.value);
      case Int64Value v -> float64Value(value * v.value);
      case Uint8Value v -> float64Value(value * v.value);
      case Uint16Value v -> float64Value(value * v.value);
      case Uint32Value v -> float64Value(value * v.value);
      case Uint64Value v -> float64Value(value * v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float64Value(value / v.toDouble());
      case Float32Value v -> float64Value(value / v.value);
      case Float64Value v -> float64Value(value / v.value);
      case Int8Value v -> float64Value(value / v.value);
      case Int16Value v -> float64Value(value / v.value);
      case Int32Value v -> float64Value(value / v.value);
      case Int64Value v -> float64Value(value / v.value);
      case Uint8Value v -> float64Value(value / v.value);
      case Uint16Value v -> float64Value(value / v.value);
      case Uint32Value v -> float64Value(value / v.value);
      case Uint64Value v -> float64Value(value / v.value.doubleValue());
    };
  }

  @Override
  public BoolValue same(ArithmeticValue other) {
    return value == other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue notSame(ArithmeticValue other) {
    return value != other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThan(ArithmeticValue other) {
    return value > other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThanOrEqual(ArithmeticValue other) {
    return value >= other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThan(ArithmeticValue other) {
    return value < other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThanOrEqual(ArithmeticValue other) {
    return value <= other.toDouble() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Float64Value other = (Float64Value) o;
    return value == other.value;
  }

  @Override
  public int hashCode() {
    return Double.hashCode(value);
  }
}
