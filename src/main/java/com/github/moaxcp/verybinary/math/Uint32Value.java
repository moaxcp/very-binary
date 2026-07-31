package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

import static com.github.moaxcp.verybinary.math.Float32Value.float32Value;
import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;
import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;
import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;
import static com.github.moaxcp.verybinary.math.Uint64Value.uint64Value;

public final class Uint32Value extends ArithmeticValue {
  final long value;

  private Uint32Value(long value) {
    if (value > 4_294_967_295L) {
      this.value = value % 4_294_967_295L;
    } else {
      this.value = value;
    }
  }

  public static Uint32Value uint32Value(long value) {
    return new Uint32Value(value);
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
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
}
