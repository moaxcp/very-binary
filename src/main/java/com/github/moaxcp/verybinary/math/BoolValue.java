package com.github.moaxcp.verybinary.math;

public final class BoolValue extends Value {
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
}
