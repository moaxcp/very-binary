package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT64_MAX;
import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;

public final class Uint64Value extends ArithmeticValue {
  final BigInteger value;

  private Uint64Value(BigInteger value) {
    if (value.compareTo(UINT64_MAX) > 0) {
      this.value = value.mod(UINT64_MAX);
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
  public boolean toBool() {
    return false;
  }

  @Override
  public byte toByte() {
    return value.byteValue();
  }

  @Override
  public short toShort() {
    return value.shortValue();
  }

  @Override
  public int toInt() {
    return value.intValue();
  }

  @Override
  public long toLong() {
    return value.longValue();
  }

  @Override
  public BigInteger toBigInteger() {
    return value;
  }

  @Override
  public float toFloat() {
    return value.floatValue();
  }

  @Override
  public double toDouble() {
    return value.doubleValue();
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> uint64Value(value.add(v.toBigInteger()));
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
      case BoolValue v -> uint64Value(value.subtract(v.toBigInteger()));
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
      case BoolValue v -> uint64Value(value.multiply(v.toBigInteger()));
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
      case BoolValue v -> uint64Value(value.divide(v.toBigInteger()));
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

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Uint64Value other = (Uint64Value) o;
    return value.equals(other.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }
}
