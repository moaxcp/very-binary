package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

import static com.github.moaxcp.verybinary.math.BoolValue.boolValue;
import static com.github.moaxcp.verybinary.math.Float32Value.float32Value;
import static com.github.moaxcp.verybinary.math.Float64Value.float64Value;
import static com.github.moaxcp.verybinary.math.Int16Value.int16Value;
import static com.github.moaxcp.verybinary.math.Int32Value.int32Value;
import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static com.github.moaxcp.verybinary.math.Uint16Value.uint16Value;
import static com.github.moaxcp.verybinary.math.Uint32Value.uint32Value;
import static com.github.moaxcp.verybinary.math.Uint64Value.uint64Value;
import static com.github.moaxcp.verybinary.math.Uint8Value.uint8Value;

public final class ValueOf implements ArithmeticExpression {

  private final int position;

  ValueOf(int position) {
    this.position = position;
  }

  public static ValueOf variable(int position) {
    return new ValueOf(position);
  }

  public int position() {
    return position;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return parent.getType(position) instanceof LengthType<?, ?> v && v.isConstant();
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    return switch (parent.getType(position)) {
      case Int8Type t -> int8Value(t.getInt8ConstantValue());
      case Int16Type t -> int16Value(t.getInt16ConstantValue());
      case Int32Type t -> int32Value(t.getInt32ConstantValue());
      case Int64Type t -> int64Value(t.getInt64ConstantValue());
      case Uint8Type t -> uint8Value(t.getUint8ConstantValue());
      case Uint16Type t -> uint16Value(t.getUint16ConstantValue());
      case Uint32Type t -> uint32Value(t.getUint32ConstantValue());
      case Uint64Type t -> uint64Value(t.getConstantValue());
      case Float32Type t -> float32Value(t.getFloat32ConstantValue());
      case Float64Type t -> float64Value(t.getFloat64ConstantValue());
      default -> throw new IllegalArgumentException("cannot evaluate " + parent + parent.getClass().getSimpleName());
    };
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    return int64Value(((LengthType<?, ?>) parent.getType(position)).defaultLengthValue());
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    var type = switch (pointer) {
      case Struct struct -> struct.getType(position);
    };
    return switch (type) {
      case Int8Type i8 -> int8Value(i8.getInt8(pointer));
      case Uint8Type u8 -> uint8Value(u8.getUint8(pointer));
      case Int16Type i16 -> int16Value(i16.getInt16(pointer));
      case Uint16Type u16 -> uint16Value(u16.getUint16(pointer));
      case Int32Type i32 -> int32Value(i32.getInt32(pointer));
      case Uint32Type u32 -> uint32Value(u32.getUint32(pointer));
      case Int64Type i64 -> int64Value(i64.getInt64(pointer));
      case Uint64Type u64 -> uint64Value(u64.get(pointer));
      case Float32Type f32 -> float32Value(f32.getFloat32(pointer));
      case Float64Type f64 -> float64Value(f64.getFloat64(pointer));
      case BoolType b -> boolValue(b.getBool(pointer));
      case ComplexType<?> ignored -> throw new IllegalArgumentException("cannot evaluate complex type");
      case PadType ignored -> throw new IllegalArgumentException("cannot evaluate pad type");
      case ListType ignored -> throw new IllegalArgumentException("cannot evaluate indexed value type");
      case null -> throw new IllegalArgumentException("cannot evaluate null type");
    };
  }

  @Override
  public String toString() {
    return "Variable(" +
        position +
        ')';
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
