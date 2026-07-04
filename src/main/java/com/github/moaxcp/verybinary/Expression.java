package com.github.moaxcp.verybinary;

import java.math.BigInteger;
import java.util.Arrays;

public interface Expression {

  class Constant implements Expression {

    private final long value;

    Constant(long value) {
      this.value = value;
    }

    @Override
    public boolean isConstant(ComplexType<?> parent) {
      return true;
    }

    @Override
    public long constantValue(ComplexType<?> parent) {
      return value;
    }

    @Override
    public long defaultValue(ComplexType<?> parent) {
      return value;
    }

    @Override
    public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
      return value;
    }

    @Override
    public String toString() {
      return "Constant{" +
          "value=" + value +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      Constant constant = (Constant) o;
      return value == constant.value;
    }

    @Override
    public int hashCode() {
      return Long.hashCode(value);
    }
  }

  class ValueOf implements Expression {

    private final int position;

    ValueOf(int position) {
      this.position = position;
    }

    @Override
    public boolean isConstant(ComplexType<?> parent) {
      return parent.getType(position) instanceof LengthType<?, ?> v && v.isConstant();
    }

    @Override
    public long constantValue(ComplexType<?> parent) {
      return switch (parent.getType(position)) {
        case Int8Type t -> t.getInt8ConstantValue();
        case Int16Type t -> t.getInt16ConstantValue();
        case Int32Type t -> t.getInt32ConstantValue();
        case Int64Type t -> t.getInt64ConstantValue();
        case Uint8Type t -> t.getUint8ConstantValue();
        case Uint16Type t -> t.getUint16ConstantValue();
        case Uint32Type t -> t.getUint32ConstantValue();
        case Uint64Type t -> t.getConstantValue().longValue();
        case Float32Type t -> (long) t.getFloat32ConstantValue();
        case Float64Type t -> (long) t.getFloat64ConstantValue();
        default -> throw new IllegalArgumentException("cannot evaluate " + parent + parent.getClass().getSimpleName());
      };
    }

    @Override
    public long defaultValue(ComplexType<?> parent) {
      return ((LengthType<?, ?>) parent.getType(position)).defaultLengthValue();
    }

    @Override
    public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
      var type = switch (pointer) {
        case Struct struct -> struct.getType(position);
      };
      return switch (type) {
        case Int8Type i8 -> i8.getInt8(pointer);
        case Uint8Type u8 -> u8.getUint8(pointer);
        case Int16Type i16 -> i16.getInt16(pointer);
        case Uint16Type u16 -> u16.getUint16(pointer);
        case Int32Type i32 -> i32.getInt32(pointer);
        case Uint32Type u32 -> u32.getUint32(pointer);
        case Int64Type i64 -> i64.getInt64(pointer);
        case Uint64Type u64 -> {
          var value = u64.get(pointer);
          if (value.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("cannot convert " + value + " to long");
          }
          yield value.longValue();
        }
        case Float32Type f32 -> (long) f32.getFloat32(pointer);
        case Float64Type f32 -> (long) f32.getFloat64(pointer);
        case BoolType ignored -> throw new IllegalArgumentException("cannot evaluate boolean type");
        case ComplexType<?> ignored -> throw new IllegalArgumentException("cannot evaluate complex type");
        case PadType ignored -> throw new IllegalArgumentException("cannot evaluate pad type");
        case ListType ignored -> throw new IllegalArgumentException("cannot evaluate indexed value type");
        case null -> throw new IllegalArgumentException("cannot evaluate null type");
      };
    }

    @Override
    public String toString() {
      return "ValueOf{" +
          "position=" + position +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      ValueOf valueOf = (ValueOf) o;
      return position == valueOf.position;
    }

    @Override
    public int hashCode() {
      return position;
    }
  }

  class Sum implements Expression {

    private final Expression[] expressions;

    Sum(Expression... expressions) {
      this.expressions = expressions;
    }

    @Override
    public boolean isConstant(ComplexType<?> parent) {
      return Arrays.stream(expressions).allMatch(e -> e.isConstant(parent));
    }

    @Override
    public long constantValue(ComplexType<?> parent) {
      return Arrays.stream(expressions).mapToLong(e -> e.constantValue(parent)).sum();
    }

    @Override
    public long defaultValue(ComplexType<?> parent) {
      return Arrays.stream(expressions).mapToLong(e -> e.defaultValue(parent)).sum();
    }

    @Override
    public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
      return Arrays.stream(expressions).mapToLong(e -> e.evaluate(pointer)).sum();
    }

    @Override
    public String toString() {
      return "Sum{" +
          "expressions=" + Arrays.toString(expressions) +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      Sum sum = (Sum) o;
      return Arrays.equals(expressions, sum.expressions);
    }

    @Override
    public int hashCode() {
      return Arrays.hashCode(expressions);
    }
  }

  static Constant constant(long value) {
    return new Constant(value);
  }

  static ValueOf valueOf(int position) {
    return new ValueOf(position);
  }

  static Sum sum(Expression... expressions) {
    return new Sum(expressions);
  }

  boolean isConstant(ComplexType<?> parent);

  long constantValue(ComplexType<?> parent);

  long defaultValue(ComplexType<?> parent);

  long evaluate(Pointer<?, ? extends Type<?>> pointer);
}
