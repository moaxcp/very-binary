package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Float32Value.float32Value;
import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;
import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;
import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;
import static com.github.moaxcp.verybinary.math.Uint32Value.uint32Value;
import static com.github.moaxcp.verybinary.math.Uint64Value.uint64Value;

public final class Int8Value extends ArithmeticValue {
  final byte value;

  private Int8Value(byte value) {
    this.value = value;
  }

  private Int8Value(int value) {
    this.value = (byte) value;
  }

  public static Int8Value int8Value(byte value) {
    return new Int8Value(value);
  }

  public static Int8Value int8Value(int value) {
    return new Int8Value(value);
  }

  @Override
  public boolean toBool() {
    return value != 0;
  }

  @Override
  public byte toByte() {
    return value;
  }

  @Override
  public short toShort() {
    return value;
  }

  @Override
  public int toInt() {
    return value;
  }

  @Override
  public long toLong() {
    return value;
  }

  @Override
  public BigInteger toBigInteger() {
    return BigInteger.valueOf(value);
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
      case BoolValue v -> int32Value(value + v.toInt());
      case Float32Value v -> float32Value(value + v.value);
      case Float64Value v -> float64Value(value + v.value);
      case Int8Value v -> int32Value(value + v.value);
      case Int16Value v -> int32Value(value + v.value);
      case Int32Value v -> int32Value(value + v.value);
      case Int64Value v -> int64Value(value + v.value);
      case Uint8Value v -> uint32Value(value + v.value);
      case Uint16Value v -> uint32Value(value + v.value);
      case Uint32Value v -> uint32Value(value + v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(value).add(v.value));
    };
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(value - v.toInt());
      case Float32Value v -> float32Value(value - v.value);
      case Float64Value v -> float64Value(value - v.value);
      case Int8Value v -> int32Value(value - v.value);
      case Int16Value v -> int32Value(value - v.value);
      case Int32Value v -> int32Value(value - v.value);
      case Int64Value v -> int64Value(value - v.value);
      case Uint8Value v -> uint32Value(value - v.value);
      case Uint16Value v -> uint32Value(value - v.value);
      case Uint32Value v -> uint32Value(value - v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(value).subtract(v.value));
    };
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(value * v.toInt());
      case Float32Value v -> float32Value(value * v.value);
      case Float64Value v -> float64Value(value * v.value);
      case Int8Value v -> int32Value(value * v.value);
      case Int16Value v -> int32Value(value * v.value);
      case Int32Value v -> int32Value(value * v.value);
      case Int64Value v -> int64Value(value * v.value);
      case Uint8Value v -> uint32Value(value * v.value);
      case Uint16Value v -> uint32Value(value * v.value);
      case Uint32Value v -> uint32Value(value * v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(value).multiply(v.value));
    };
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(value / v.toInt());
      case Float32Value v -> float32Value(value / v.value);
      case Float64Value v -> float64Value(value / v.value);
      case Int8Value v -> int32Value(value / v.value);
      case Int16Value v -> int32Value(value / v.value);
      case Int32Value v -> int32Value(value / v.value);
      case Int64Value v -> int64Value(value / v.value);
      case Uint8Value v -> uint32Value(value / v.value);
      case Uint16Value v -> uint32Value(value / v.value);
      case Uint32Value v -> uint32Value(value / v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(value).divide(v.value));
    };
  }

  @Override
  public BoolValue same(ArithmeticValue other) {
    return value == other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue notSame(ArithmeticValue other) {
    return value != other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThan(ArithmeticValue other) {
    return value > other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThanOrEqual(ArithmeticValue other) {
    return value >= other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThan(ArithmeticValue other) {
    return value < other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThanOrEqual(ArithmeticValue other) {
    return value <= other.toByte() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    Int8Value other = (Int8Value) o;
    return value == other.value;
  }

  @Override
  public int hashCode() {
    return Byte.hashCode(value);
  }
}
