package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;

public final class Float32Value extends ArithmeticValue {
  final float value;

  private Float32Value(float value) {
    this.value = value;
  }

  public static Float32Value float32Value(float value) {
    return new Float32Value(value);
  }

  @Override
  public boolean toBool() {
    return false;
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
    return value;
  }

  @Override
  public double toDouble() {
    return value;
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float32Value(value + v.toFloat());
      case Float32Value v -> float32Value(value + v.value);
      case Float64Value v -> float64Value(value + v.value);
      case Int8Value v -> float32Value(value + v.value);
      case Int16Value v -> float32Value(value + v.value);
      case Int32Value v -> float32Value(value + v.value);
      case Int64Value v -> float64Value(value + v.value);
      case Uint8Value v -> float32Value(value + v.value);
      case Uint16Value v -> float32Value(value + v.value);
      case Uint32Value v -> float32Value(value + v.value);
      case Uint64Value v -> float64Value(value + v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float32Value(value - v.toFloat());
      case Float32Value v -> float32Value(value - v.value);
      case Float64Value v -> float64Value(value - v.value);
      case Int8Value v -> float32Value(value - v.value);
      case Int16Value v -> float32Value(value - v.value);
      case Int32Value v -> float32Value(value - v.value);
      case Int64Value v -> float64Value(value - v.value);
      case Uint8Value v -> float32Value(value - v.value);
      case Uint16Value v -> float32Value(value - v.value);
      case Uint32Value v -> float32Value(value - v.value);
      case Uint64Value v -> float64Value(value - v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float32Value(value * v.toFloat());
      case Float32Value v -> float32Value(value * v.value);
      case Float64Value v -> float64Value(value * v.value);
      case Int8Value v -> float32Value(value * v.value);
      case Int16Value v -> float32Value(value * v.value);
      case Int32Value v -> float32Value(value * v.value);
      case Int64Value v -> float64Value(value * v.value);
      case Uint8Value v -> float32Value(value * v.value);
      case Uint16Value v -> float32Value(value * v.value);
      case Uint32Value v -> float32Value(value * v.value);
      case Uint64Value v -> float64Value(value * v.value.doubleValue());
    };
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> float32Value(value / v.toFloat());
      case Float32Value v -> float32Value(value / v.value);
      case Float64Value v -> float64Value(value / v.value);
      case Int8Value v -> float32Value(value / v.value);
      case Int16Value v -> float32Value(value / v.value);
      case Int32Value v -> float32Value(value / v.value);
      case Int64Value v -> float64Value(value / v.value);
      case Uint8Value v -> float32Value(value / v.value);
      case Uint16Value v -> float32Value(value / v.value);
      case Uint32Value v -> float32Value(value / v.value);
      case Uint64Value v -> float64Value(value / v.value.doubleValue());
    };
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Float32Value other = (Float32Value) o;
    return value == other.value;
  }

  @Override
  public int hashCode() {
    return Float.hashCode(value);
  }
}
