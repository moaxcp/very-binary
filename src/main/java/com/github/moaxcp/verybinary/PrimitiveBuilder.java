package com.github.moaxcp.verybinary;

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
      case boolean[] b -> (T) b;
      case byte[] b -> (T) b;
      case short[] s -> (T) s;
      case int[] i -> (T) i;
      case long[] l -> (T) l;
      case float[] f -> (T) f;
      case double[] d -> (T) d;
      case List l -> (T) l;
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
      return new BoolArrayType(position, getConstantValue(boolean[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new BoolType(position, getConstantValue(Boolean.class));
    }
  }

  public Type<?> int8() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Int8ArrayType(position, getConstantValue(byte[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int8Type(position, getConstantValue(Byte.class));
    }
  }

  public Type<?> uint8() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Uint8ArrayType(position, getConstantValue(short[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint8Type(position, getConstantValue(Short.class));
    }
  }

  public Type<?> int16() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Int16ArrayType(position, getConstantValue(short[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int16Type(position, getConstantValue(Short.class));
    }
  }

  public Type<?> uint16() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Uint16ArrayType(position, getConstantValue(int[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint16Type(position, getConstantValue(Integer.class));
    }
  }

  public Type<?> int32() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Int32ArrayType(position, getConstantValue(int[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int32Type(position, getConstantValue(Integer.class));
    }
  }

  public Type<?> uint32() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Uint32ArrayType(position, getConstantValue(long[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint32Type(position, getConstantValue(Long.class));
    }
  }

  public Type<?> int64() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Int64ArrayType(position, getConstantValue(long[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Int64Type(position, getConstantValue(Long.class));
    }
  }

  public Type<?> uint64() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Uint64ListType(position, getConstantValue(List.class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Uint64Type(position, getConstantValue(BigInteger.class));
    }
  }

  public Type<?> float32() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Float32ArrayType(position, getConstantValue(float[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Float32Type(position, getConstantValue(Float.class));
    }
  }

  public Type<?> float64() {
    if (lengthExpression != null || byteLengthExpression != null) {
      return new Float64ArrayType(position, getConstantValue(double[].class), lengthExpression, byteLengthExpression)
          .addLengthChangeListeners(lengthListeners)
          .addByteLengthChangeListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    } else {
      return new Float64Type(position, getConstantValue(Double.class));
    }
  }
}
