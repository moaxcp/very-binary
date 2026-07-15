package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

import java.math.BigInteger;

public final class Variable implements Expression {

  private final int position;

  Variable(int position) {
    this.position = position;
  }

  public int position() {
    return position;
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

    Variable variable = (Variable) o;
    return position == variable.position;
  }

  @Override
  public int hashCode() {
    return position;
  }
}
