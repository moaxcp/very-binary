package com.github.moaxcp.verybinary;

import java.math.BigInteger;

public enum Primitive {
  BOOL(1),
  INT8(1),
  UINT8(1),
  INT16(2),
  UINT16(2),
  INT32(4),
  UINT32(4),
  INT64(8),
  UINT64(8),
  FLOAT32(4),
  FLOAT64(8);

  private final int size;

  Primitive(int size) {
    this.size = size;
  }

  public int size() {
    return size;
  }

  public String label() {
    return toString().toLowerCase();
  }

  public String title() {
    return toString().substring(0, 1).toUpperCase() + toString().substring(1).toLowerCase();
  }

  public String primitive() {
    return switch(this) {
      case BOOL -> "boolean";
      case INT8 -> "byte";
      case UINT8, INT16 -> "short";
      case UINT16, INT32 -> "int";
      case UINT32, INT64 -> "long";
      case UINT64 -> "BigInteger";
      case FLOAT32 -> "float";
      case FLOAT64 -> "double";
    };
  }

  public String wrapper() {
    return switch(this) {
      case BOOL -> "Boolean";
      case INT8 -> "Byte";
      case UINT8, INT16 -> "Short";
      case UINT16, INT32 -> "Integer";
      case UINT32, INT64 -> "Long";
      case UINT64 -> "BigInteger";
      case FLOAT32 -> "Float";
      case FLOAT64 -> "Double";
    };
  }

  public static boolean isUint8(int value) {
    return value >= 0 && value <= 0xFF;
  }

  public static boolean isUint16(int value) {
    return value >= 0 && value <= 0xFFFF;
  }

  public static boolean isUint32(long value) {
    return value >= 0 && value <= 0xFFFF_FFFFL;
  }

  public static boolean isUint64(java.math.BigInteger value) {
    if (value == null) return false;
    return value.signum() >= 0 && value.compareTo(new java.math.BigInteger("18446744073709551615")) <= 0;
  }

  public static void requireUint8(int value) {
    if (!isUint8(value)) {
      throw new IllegalArgumentException("uint8 out of range: " + value);
    }
  }

  public static void requireUint8(short[] values) {
    if(values != null) {
      for(int v : values) {
        requireUint8(v);
      }
    }
  }

  public static void requireUint16(int value) {
    if (!isUint16(value)) {
      throw new IllegalArgumentException("uint16 out of range: " + value);
    }
  }

  public static void requireUint16(int[] values) {
    if(values != null) {
      for(int v : values) {
        requireUint16(v);
      }
    }
  }

  public static void requireUint32(long value) {
    if (!isUint32(value)) {
      throw new IllegalArgumentException("uint32 out of range: " + value);
    }
  }

  public static void requireUint32(long[] values) {
    if(values != null) {
      for(long v : values) {
        requireUint32(v);
      }
    }
  }

  public static void requireUint64(BigInteger value) {
    if (!isUint64(value)) {
      throw new IllegalArgumentException("uint64 out of range: " + value);
    }
  }

  public static void requireUint64(BigInteger[] values) {
    if (values != null) {
      for (var value : values) {
        requireUint64(value);
      }
    }
  }
}
