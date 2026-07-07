package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.*;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class PrimitiveBuilder {

  public static PrimitiveBuilder primitive() {
    return new PrimitiveBuilder();
  }

  private int position = -1;
  private final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();
  private final List<LengthListener> lengthListeners = new ArrayList<>();
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  @Nullable
  private Object constantValue;
  @Nullable
  private Expression lengthExpression;
  @Nullable
  private Expression byteLengthExpression;

  public PrimitiveBuilder position(int position) {
    this.position = position;
    return this;
  }

  public PrimitiveBuilder byteLengthListener(ByteLengthListener byteLengthListener) {
    byteLengthListeners.add(byteLengthListener);
    return this;
  }

  public PrimitiveBuilder arrayLengthListener(LengthListener lengthListener) {
    lengthListeners.add(lengthListener);
    return this;
  }

  public PrimitiveBuilder valueListener(ValueChangeListener valueListener) {
    valueChangeListeners.add(valueListener);
    return this;
  }

  public PrimitiveBuilder constant(Object constantValue) {
    this.constantValue = constantValue;
    return this;
  }

  public PrimitiveBuilder lengthExpression(Expression lengthExpression) {
    this.lengthExpression = lengthExpression;
    return this;
  }

  public PrimitiveBuilder byteLengthExpression(Expression byteLengthExpression) {
    this.byteLengthExpression = byteLengthExpression;
    return this;
  }

  private <T> T getConstantValue(Class<T> clazz) {
    return switch (constantValue) {
      case BinaryList l -> (T) l;
      case Number n -> {
        if (clazz == Byte.class) {
          yield (T) Byte.valueOf(n.byteValue());
        } else if (clazz == Short.class) {
          yield (T) Short.valueOf(n.shortValue());
        } else if (clazz == Integer.class) {
          yield (T) Integer.valueOf(n.intValue());
        } else if (clazz == Long.class) {
          yield (T) Long.valueOf(n.longValue());
        } else if (clazz == BigInteger.class) {
          yield (T) BigInteger.valueOf(n.longValue());
        } else if (clazz == Float.class) {
          yield (T) Float.valueOf(n.floatValue());
        } else if (clazz == Double.class) {
          yield (T) Double.valueOf(n.doubleValue());
        } else {
          throw new IllegalArgumentException("constant value is not of type " + clazz.getSimpleName() + " but " + n.getClass().getSimpleName());
        }
      }
      case Boolean b -> (T) b;
      case null -> null;
      default -> throw new IllegalStateException("Unexpected value: " + constantValue + " class: " + clazz);
    };
  }

  public Type<?> bool() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof boolean[]) {
      return new BoolListType(position, null, getConstantValue(BoolList.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new BoolType(position, null , getConstantValue(Boolean.class));
    }
  }

  public Type<?> int8() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof byte[] || constantValue instanceof com.github.moaxcp.verybinary.list.Int8List) {
      return new Int8ListType(position, null, (com.github.moaxcp.verybinary.list.Int8List) constantValue, lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int8Type(position, null, getConstantValue(Byte.class));
    }
  }

  public Type<?> uint8() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof short[]) {
      return new Uint8ListType(position, null, getConstantValue(Uint8List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint8Type(position, null, getConstantValue(Short.class));
    }
  }

  public Type<?> int16() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof short[]) {
      return new Int16ListType(position, null, getConstantValue(Int16List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int16Type(position, null, getConstantValue(Short.class));
    }
  }

  public Type<?> uint16() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof int[]) {
      return new Uint16ListType(position, null, getConstantValue(Uint16List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint16Type(position, null, getConstantValue(Integer.class));
    }
  }

  public Type<?> int32() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof int[]) {
      return new Int32ListType(position, null, getConstantValue(Int32List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int32Type(position, null, getConstantValue(Integer.class));
    }
  }

  public Type<?> uint32() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof long[]) {
      return new Uint32ListType(position, null, getConstantValue(Uint32List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint32Type(position, null, getConstantValue(Long.class));
    }
  }

  public Type<?> int64() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof long[]) {
      return new Int64ListType(position, null, getConstantValue(Int64List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int64Type(position, null, getConstantValue(Long.class));
    }
  }

  public Type<?> uint64() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof List) {
      return new Uint64ListType(position, null, getConstantValue(Uint64List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint64Type(position, null, getConstantValue(BigInteger.class));
    }
  }

  public Type<?> float32() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof float[]) {
      return new Float32ListType(position, null, getConstantValue(Float32List.class), lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Float32Type(position, null, getConstantValue(Float.class));
    }
  }

  public Type<?> float64() {
    if (lengthExpression != null || byteLengthExpression != null || constantValue != null && constantValue instanceof double[]) {
      return new Float64ListType(position, null, (Float64List) constantValue, lengthExpression)
          .addLengthListeners(lengthListeners)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Float64Type(position, null, getConstantValue(Double.class));
    }
  }
}
