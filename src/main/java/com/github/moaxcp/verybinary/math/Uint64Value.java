package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;

public final class Uint64Value extends ArithmeticValue {
  private static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");
  final BigInteger value;

  private Uint64Value(BigInteger value) {
    if (value.compareTo(MAX_VALUE) > 0) {
      this.value = value.mod(MAX_VALUE);
    } else {
      this.value = value;
    }
  }

  private Uint64Value(long value) {
    this(BigInteger.valueOf(value));
  }

  public static Uint64Value uint64Value(BigInteger value) {
    return new Uint64Value(value);
  }

  public static Uint64Value uint64Value(long value) {
    return new Uint64Value(value);
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
      case Float32Value v -> uint64Value(value.add(BigInteger.valueOf((long) v.value)));
      case Float64Value v -> uint64Value(value.add(BigInteger.valueOf((long) v.value)));
      case Int8Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Int16Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Int32Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Int64Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Uint8Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Uint16Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Uint32Value v -> uint64Value(value.add(BigInteger.valueOf(v.value)));
      case Uint64Value v -> uint64Value(value.add(v.value));
    };
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue other) {
    return switch (other) {
      case Float32Value v -> uint64Value(value.subtract(BigInteger.valueOf((long) v.value)));
      case Float64Value v -> uint64Value(value.subtract(BigInteger.valueOf((long) v.value)));
      case Int8Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Int16Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Int32Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Int64Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Uint8Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Uint16Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Uint32Value v -> uint64Value(value.subtract(BigInteger.valueOf(v.value)));
      case Uint64Value v -> uint64Value(value.subtract(v.value));
    };
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue other) {
    return switch (other) {
      case Float32Value v -> uint64Value(value.multiply(BigInteger.valueOf((long) v.value)));
      case Float64Value v -> float64Value(value.doubleValue() * v.value);
      case Int8Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Int16Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Int32Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Int64Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Uint8Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Uint16Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Uint32Value v -> uint64Value(value.multiply(BigInteger.valueOf(v.value)));
      case Uint64Value v -> uint64Value(value.multiply(v.value));
    };
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue other) {
    return switch (other) {
      case Float32Value v -> uint64Value(value.divide(BigInteger.valueOf((long) v.value)));
      case Float64Value v -> float64Value(value.doubleValue() / v.value);
      case Int8Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Int16Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Int32Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Int64Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Uint8Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Uint16Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Uint32Value v -> uint64Value(value.divide(BigInteger.valueOf(v.value)));
      case Uint64Value v -> uint64Value(value.divide(v.value));
    };
  }
}
