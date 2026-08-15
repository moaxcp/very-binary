package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Float32Value.float32Value;
import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;
import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;
import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;
import static com.github.moaxcp.verybinary.math.Uint32Value.uint32Value;
import static com.github.moaxcp.verybinary.math.Uint64Value.uint64Value;

public final class BoolValue extends ArithmeticValue {
  public static final BoolValue TRUE = boolValue(true);
  public static final BoolValue FALSE = boolValue(false);
  final boolean value;

  private BoolValue(boolean value) {
    this.value = value;
  }

  public static BoolValue boolValue(boolean value) {
    return new BoolValue(value);
  }

  public BoolValue and(BoolValue other) {
    return new BoolValue(value && other.value);
  }

  public BoolValue or(BoolValue other) {
    return new BoolValue(value || other.value);
  }

  public BoolValue xor(BoolValue other) {
    return new BoolValue(value ^ other.value);
  }

  public BoolValue not() {
    return new BoolValue(!value);
  }

  @Override
  public boolean toBool() {
    return value;
  }

  @Override
  public byte toByte() {
    return (byte) toInt();
  }

  @Override
  public short toShort() {
    return (short) toInt();
  }

  @Override
  public int toInt() {
    return value ? 1 : 0;
  }

  @Override
  public long toLong() {
    return toInt();
  }

  @Override
  public BigInteger toBigInteger() {
    return value ? BigInteger.ONE : BigInteger.ZERO;
  }

  @Override
  public float toFloat() {
    return value ? 1.0f : 0.0f;
  }

  @Override
  public double toDouble() {
    return value ? 1.0 : 0.0;
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(toInt() + v.toInt());
      case Float32Value v -> float32Value(toInt() + v.value);
      case Float64Value v -> float64Value(toInt() + v.value);
      case Int8Value v -> int32Value(toInt() + v.value);
      case Int16Value v -> int32Value(toInt() + v.value);
      case Int32Value v -> int32Value(toInt() + v.value);
      case Int64Value v -> int64Value(toInt() + v.value);
      case Uint8Value v -> uint32Value(toInt() + v.value);
      case Uint16Value v -> uint32Value(toInt() + v.value);
      case Uint32Value v -> uint32Value(toInt() + v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(toInt()).add(v.value));
    };
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(toInt() * v.toInt());
      case Float32Value v -> float32Value(toInt() * v.value);
      case Float64Value v -> float64Value(toInt() * v.value);
      case Int8Value v -> int32Value(toInt() * v.value);
      case Int16Value v -> int32Value(toInt() * v.value);
      case Int32Value v -> int32Value(toInt() * v.value);
      case Int64Value v -> int64Value(toInt() * v.value);
      case Uint8Value v -> uint32Value(toInt() * v.value);
      case Uint16Value v -> uint32Value(toInt() * v.value);
      case Uint32Value v -> uint32Value(toInt() * v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(toInt()).multiply(v.value));
    };
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(toInt() - v.toInt());
      case Float32Value v -> float32Value(toInt() - v.value);
      case Float64Value v -> float64Value(toInt() - v.value);
      case Int8Value v -> int32Value(toInt() - v.value);
      case Int16Value v -> int32Value(toInt() - v.value);
      case Int32Value v -> int32Value(toInt() - v.value);
      case Int64Value v -> int64Value(toInt() - v.value);
      case Uint8Value v -> uint32Value(toInt() - v.value);
      case Uint16Value v -> uint32Value(toInt() - v.value);
      case Uint32Value v -> uint32Value(toInt() - v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(toInt()).subtract(v.value));
    };
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue other) {
    return switch (other) {
      case BoolValue v -> int32Value(toInt() / v.toInt());
      case Float32Value v -> float32Value(toInt() / v.value);
      case Float64Value v -> float64Value(toInt() / v.value);
      case Int8Value v -> int32Value(toInt() / v.value);
      case Int16Value v -> int32Value(toInt() / v.value);
      case Int32Value v -> int32Value(toInt() / v.value);
      case Int64Value v -> int64Value(toInt() / v.value);
      case Uint8Value v -> uint32Value(toInt() / v.value);
      case Uint16Value v -> uint32Value(toInt() / v.value);
      case Uint32Value v -> uint32Value(toInt() / v.value);
      case Uint64Value v -> uint64Value(BigInteger.valueOf(toInt()).divide(v.value));
    };
  }

  @Override
  public BoolValue same(ArithmeticValue other) {
    return value == other.toBool() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue notSame(ArithmeticValue other) {
    return value != other.toBool() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThan(ArithmeticValue other) {
    return toInt() > other.toInt() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue greaterThanOrEqual(ArithmeticValue other) {
    return toInt() >= other.toInt() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThan(ArithmeticValue other) {
    return toInt() < other.toInt() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public BoolValue lessThanOrEqual(ArithmeticValue other) {
    return toInt() <= other.toInt() ? BoolValue.TRUE : BoolValue.FALSE;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    BoolValue boolValue = (BoolValue) o;
    return value == boolValue.value;
  }

  @Override
  public int hashCode() {
    return Boolean.hashCode(value);
  }
}
