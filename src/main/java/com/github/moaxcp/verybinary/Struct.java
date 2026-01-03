package com.github.moaxcp.verybinary;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;

public class Struct implements Pointer<Struct, StructType> {
  private boolean allocated = false;
  private long offset;
  private StructType structType;
  private ByteArray bytes;
  private ByteArrayListener listener;

   public Struct(Struct struct) {
    this(struct.allocated, struct.offset, struct.structType, struct.bytes.copy());
   }

  public Struct(StructType structType) {
    this(0, structType);
  }

  public Struct(StructType structType, ByteArray bytes) {
    this(true, 0, structType, bytes);
  }

  public Struct(int offset, StructType structType) {
    this(false, offset, structType, new ByteArray());
  }

  public Struct(long offset, StructType structType, ByteArray bytes) {
    this(true, offset, structType, bytes);
  }

  public Struct(boolean allocated, long offset, StructType structType, ByteArray bytes) {
     this.allocated = allocated;
    this.offset = offset;
    this.structType = structType;
    this.bytes = bytes;
    if (!this.allocated) {
      structType.allocate(this);
    }
    listener = shift -> {
      var o = getOffset();
      if (o >= shift.offset()) {
        setOffset(o + shift.size());
      }
    };
    bytes.addListener(listener);
  }

  @Override
  public Struct copy() {
    return new Struct(this);
  }

  @Override
  public long getOffset() {
    return offset;
  }

  @Override
  public Struct setOffset(long offset) {
    this.offset = offset;
    return this;
  }

  @Override
  public StructType getType() {
    return structType;
  }

  @Override
  public <V extends Type<?>> V getType(int position) {
    return structType.getType(position);
  }

  @Override
  public int getPositions() {
    return structType.getPositions();
  }

  @Override
  public ByteArray getByteArray() {
    return bytes;
  }

  @Override
  public void setByteArray(ByteArray bytes) {
    bytes.removeListener(listener);
    this.bytes = bytes;
    bytes.addListener(listener);
  }

  public long getByteLength() {
    return structType.getByteLength(this);
  }

  @Override
  public long getByteLength(int position) {
    return structType.getFieldByteLength(this, position);
  }

  @Override
  public long getByteLength(int position, long index) {
     return structType.getFieldByteLength(this, position, index);
  }

  @Override
  public long getByteLength(int position, long index, long length) {
     return structType.getFieldByteLength(this, position, index, length);
  }

  @Override
  public long getArrayLength(int position) {
    return ((ValueType) structType.getType(position)).getArrayLength(this);
  }

  public boolean getBool(int position) {
    return ((BoolType) structType.getType(position)).getBool(this);
  }

  public Struct setBool(int position, boolean b) {
    ((BoolType) structType.getType(position)).set(this, b);
    return this;
  }

  public boolean getBool(int position, long index) {
    return ((BoolType) structType.getType(position)).getBool(this, index);
  }

  public Struct setBool(int position, long index, boolean b) {
    ((BoolType) structType.getType(position)).set(this, index, b);
    return this;
  }
  
  public boolean[] getBoolArray(int position) {
    return ((BoolType) structType.getType(position)).getBoolArray(this);
  }

  public Struct setBool(int position, boolean... values) {
    ((BoolType) structType.getType(position)).set(this, values);
    return this;
  }
  
  public boolean[] getBoolArray(int position, long index, long length) {
    return ((BoolType) structType.getType(position)).getBoolArray(this, index, length);
  }
  
  public Struct setBool(int position, long index, boolean... values) {
    ((BoolType) structType.getType(position)).set(this, index, values);
    return this;
  }
  
  public List<Boolean> getBoolList(int position) {
    return ((BoolType) structType.getType(position)).getBoolList(this);
  }
  
  public Struct setBool(int position, List<Boolean> values) {
    ((BoolType) structType.getType(position)).set(this, values);
    return this;
  }
  
  public List<Boolean> getBoolList(int position, long index, long length) {
    return ((BoolType) structType.getType(position)).getBoolList(this, index, length);
  }
  
  public Struct setBool(int position, long index, List<Boolean> values) {
    ((BoolType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addBool(int position, boolean b) {
    ((BoolType) structType.getType(position)).add(this, b);
    return this;
  }

  public Struct addBool(int position, long index, boolean b) {
    ((BoolType) structType.getType(position)).add(this, index, b);
    return this;
  }

  public Struct addBool(int position, boolean... values) {
     ((BoolType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct addBool(int position, long index, boolean... values) {
     ((BoolType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public Struct addBool(int position, List<Boolean> values) {
     ((BoolType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct addBool(int position, long index, List<Boolean> values) {
     ((BoolType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public byte getInt8(int position) {
    return ((Int8Type) structType.getType(position)).getInt8(this);
  }

  public byte getInt8(int position, long index) {
    return ((Int8Type) structType.getType(position)).getInt8(this, index);
  }

  public Struct setInt8(int position, byte b) {
    ((Int8Type) structType.getType(position)).set(this, b);
    return this;
  }

  public Struct setInt8(int position, int b) {
     return setInt8(position, (byte) b);
  }

  public Struct setInt8(int position, long index, byte b) {
    ((Int8Type) structType.getType(position)).set(this, index, b);
    return this;
  }

  public Struct setInt8(int position, long index, int b) {
     return setInt8(position, index, (byte) b);
  }

  public Struct addInt8(int position, byte b) {
    ((Int8Type) structType.getType(position)).add(this, b);
    return this;
  }

  public Struct addInt8(int position, long index, byte b) {
    ((Int8Type) structType.getType(position)).add(this, index, b);
    return this;
  }

  public short getUint8(int position) {
    return ((Uint8Type) structType.getType(position)).getUint8(this);
  }

  public short getUint8(int position, long index) {
    return ((Uint8Type) structType.getType(position)).getUint8(this, index);
  }

  public Struct setUint8(int position, short s) {
    ((Uint8Type) structType.getType(position)).set(this, s);
    return this;
  }

  public Struct setUint8(int position, int s) {
     return setUint8(position, s);
  }

  public Struct setUint8(int position, long index, short s) {
    ((Uint8Type) structType.getType(position)).set(this, index, s);
    return this;
  }

  public Struct setUint8(int position, long index, int s) {
     return setUint8(position, index, (short) s);
  }

  public Struct addUint8(int position, short s) {
    ((Uint8Type) structType.getType(position)).add(this, s);
    return this;
  }

  public Struct addUint8(int position, long index, short s) {
    ((Uint8Type) structType.getType(position)).add(this, index, s);
    return this;
  }

  public short getInt16(int position) {
    return ((Int16Type) structType.getType(position)).getInt16(this);
  }

  public short getInt16(int position, long index) {
    return ((Int16Type) structType.getType(position)).getInt16(this, index);
  }

  public Struct setInt16(int position, short s) {
    ((Int16Type) structType.getType(position)).set(this, s);
    return this;
  }

  public Struct setInt16(int position, int s) {
     return setInt16(position, (short) s);
  }

  public Struct setInt16(int position, long index, short s) {
    ((Int16Type) structType.getType(position)).set(this, index, s);
    return this;
  }

  public Struct setInt16(int position, long index, int s) {
     return setInt16(position, index, (short) s);
  }

  public Struct addInt16(int position, short s) {
    ((Int16Type) structType.getType(position)).add(this, s);
    return this;
  }

  public Struct addInt16(int position, long index, short s) {
    ((Int16Type) structType.getType(position)).add(this, index, s);
    return this;
  }

  public int getUint16(int position) {
    return ((Uint16Type) structType.getType(position)).getUint16(this);
  }

  public int getUint16(int position, long index) {
    return ((Uint16Type) structType.getType(position)).getUint16(this, index);
  }

  public Struct setUint16(int position, int i) {
    ((Uint16Type) structType.getType(position)).set(this, i);
    return this;
  }

  public Struct setUint16(int position, long index, int i) {
    ((Uint16Type) structType.getType(position)).set(this, index, i);
    return this;
  }

  public Struct addUint16(int position, int i) {
    ((Uint16Type) structType.getType(position)).add(this, i);
    return this;
  }

  public Struct addUint16(int position, long index, int i) {
    ((Uint16Type) structType.getType(position)).add(this, index, i);
    return this;
  }

  public int getInt32(int position) {
    return ((Int32Type) structType.getType(position)).getInt32(this);
  }

  public int getInt32(int position, long index) {
    return ((Int32Type) structType.getType(position)).getInt32(this, index);
  }

  public Struct setInt32(int position, int i) {
    ((Int32Type) structType.getType(position)).set(this, i);
    return this;
  }

  public Struct setInt32(int position, long index, int i) {
    ((Int32Type) structType.getType(position)).set(this, index, i);
    return this;
  }

  public Struct addInt32(int position, int i) {
    ((Int32Type) structType.getType(position)).add(this, i);
    return this;
  }

  public Struct addInt32(int position, long index, int i) {
    ((Int32Type) structType.getType(position)).add(this, index, i);
    return this;
  }

  public long getUint32(int position) {
    return ((Uint32Type) structType.getType(position)).getUint32(this);
  }

  public long getUint32(int position, long index) {
    return ((Uint32Type) structType.getType(position)).getUint32(this, index);
  }

  public Struct setUint32(int position, long l) {
    ((Uint32Type) structType.getType(position)).set(this, l);
    return this;
  }

  public Struct setUint32(int position, long index, long l) {
    ((Uint32Type) structType.getType(position)).set(this, index, l);
    return this;
  }

  public Struct addUint32(int position, long l) {
    ((Uint32Type) structType.getType(position)).add(this, l);
    return this;
  }

  public Struct addUint32(int position, long index, long l) {
    ((Uint32Type) structType.getType(position)).add(this, index, l);
    return this;
  }

  public long getInt64(int position) {
    return ((Int64Type) structType.getType(position)).getInt64(this);
  }

  public long getInt64(int position, long index) {
    return ((Int64Type) structType.getType(position)).getInt64(this, index);
  }

  public Struct setInt64(int position, long l) {
    ((Int64Type) structType.getType(position)).set(this, l);
    return this;
  }

  public Struct setInt64(int position, long index, long l) {
    ((Int64Type) structType.getType(position)).set(this, index, l);
    return this;
  }

  public Struct addInt64(int position, long l) {
    ((Int64Type) structType.getType(position)).add(this, l);
    return this;
  }

  public Struct addInt64(int position, long index, long l) {
    ((Int64Type) structType.getType(position)).add(this, index, l);
    return this;
  }

  public BigInteger getUint64(int position) {
    return ((Uint64Type) structType.getType(position)).getUint64(this);
  }

  public BigInteger getUint64(int position, long index) {
    return ((Uint64Type) structType.getType(position)).getUint64(this, index);
  }

  public Struct setUint64(int position, BigInteger bi) {
    ((Uint64Type) structType.getType(position)).set(this, bi);
    return this;
  }

  public Struct setUint64(int position, long bi) {
     return setUint64(position, BigInteger.valueOf(bi));
  }

  public Struct setUint64(int position, long index, BigInteger bi) {
    ((Uint64Type) structType.getType(position)).set(this, index, bi);
    return this;
  }

  public Struct setUint64(int position, long index, long bi) {
     return setUint64(position, index, BigInteger.valueOf(bi));
  }

  public Struct addUint64(int position, BigInteger bi) {
    ((Uint64Type) structType.getType(position)).add(this, bi);
    return this;
  }

  public Struct addUint64(int position, long index, BigInteger bi) {
    ((Uint64Type) structType.getType(position)).add(this, index, bi);
    return this;
  }

  public float getFloat32(int position) {
    return ((Float32Type) structType.getType(position)).getFloat32(this);
  }

  public float getFloat32(int position, long index) {
    return ((Float32Type) structType.getType(position)).getFloat32(this, index);
  }

  public Struct setFloat32(int position, float f) {
    ((Float32Type) structType.getType(position)).set(this, f);
    return this;
  }

  public Struct setFloat32(int position, long index, float f) {
    ((Float32Type) structType.getType(position)).set(this, index, f);
    return this;
  }

  public Struct addFloat32(int position, float f) {
    ((Float32Type) structType.getType(position)).add(this, f);
    return this;
  }

  public Struct addFloat32(int position, long index, float f) {
    ((Float32Type) structType.getType(position)).add(this, index, f);
    return this;
  }

  public double getFloat64(int position) {
    return ((Float64Type) structType.getType(position)).getFloat64(this);
  }

  public double getFloat64(int position, long index) {
    return ((Float64Type) structType.getType(position)).getFloat64(this, index);
  }

  public Struct setFloat64(int position, double d) {
    ((Float64Type) structType.getType(position)).set(this, d);
    return this;
  }

  public Struct setFloat64(int position, long index, double d) {
    ((Float64Type) structType.getType(position)).set(this, index, d);
    return this;
  }

  public Struct addFloat64(int position, double d) {
    ((Float64Type) structType.getType(position)).add(this, d);
    return this;
  }

  public Struct addFloat64(int position, long index, double d) {
    ((Float64Type) structType.getType(position)).add(this, index, d);
    return this;
  }

  public Struct getStruct(int position) {
    return ((StructType) structType.getType(position)).get(this);
  }

  public Struct setStruct(int position, Struct other) {
    ((StructType) structType.getType(position)).set(this, other);
    return this;
  }

  public Struct addStruct(int position, Struct struct) {
    ((StructType) structType.getType(position)).add(this, struct);
     return this;
  }

  public Struct addStruct(int position, long index, Struct struct) {
    ((StructType) structType.getType(position)).add(this, index, struct);
    return this;
  }

  public void removeAll(int position) {
    ((ValueType<?, ?>) structType.getType(position)).remove(this);
  }

  public void remove(int position, long index) {
     if (structType.getType(position) instanceof ValueType<?, ?> valueType) {
       valueType.remove(this, index);
     } else {
       throw new IllegalArgumentException("Field at postion " + position + " is not a ValueType");
     }
  }

  public void remove(int position, long index, long length) {
     if (structType.getType(position) instanceof ValueType<?, ?> valueType) {
       valueType.remove(this, index, length);
     } else {
       throw new IllegalArgumentException("Field at postion " + position + " is not a ValueType");
     }
  }

  public Struct with(Consumer<Struct> consumer) {
     consumer.accept(this);
     return this;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Struct struct)) return false;

    return offset == struct.offset && structType.equals(struct.structType) && getByteArray().compareBytes(getOffset(), struct.getByteArray(), struct.getOffset(), getByteLength());
  }

  @Override
  public int hashCode() {
    int result = Math.toIntExact(offset);
    result = 31 * result + structType.hashCode();
    for (int i = 0; i < getByteLength(); i++) {
      result = Math.toIntExact(31 * result + bytes.getInt8(getOffset() + i));
    }
    return result;
  }
}
