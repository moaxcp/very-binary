package com.github.moaxcp.verybinary.math;

public final class Float64Value extends ArithmeticValue {
  final double value;

  private Float64Value(double value) {
    this.value = value;
  }

  public static Float64Value float64Value(double value) {
    return new Float64Value(value);
  }

  @Override
  public ArithmeticValue sum(ArithmeticValue other) {
    return switch (other) {
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
}
