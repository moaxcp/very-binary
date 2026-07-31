package com.github.moaxcp.verybinary.math;

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
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
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
}
