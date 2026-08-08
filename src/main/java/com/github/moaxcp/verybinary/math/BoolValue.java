package com.github.moaxcp.verybinary.math;

import java.math.BigInteger;

public final class BoolValue extends ArithmeticValue {
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
  public ArithmeticValue sum(ArithmeticValue value) {
    return null;
  }

  @Override
  public ArithmeticValue multiply(ArithmeticValue value) {
    return null;
  }

  @Override
  public ArithmeticValue subtract(ArithmeticValue value) {
    return null;
  }

  @Override
  public ArithmeticValue divide(ArithmeticValue value) {
    return null;
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
