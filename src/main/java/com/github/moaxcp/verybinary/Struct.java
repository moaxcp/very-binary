package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.BinaryList;
import com.github.moaxcp.verybinary.list.Float32List;
import com.github.moaxcp.verybinary.list.Float64List;

import java.util.List;

import static com.github.moaxcp.verybinary.Util.mapIntsToBytes;
import static com.github.moaxcp.verybinary.Util.mapIntsToShorts;

public final class Struct implements ComplexPointer<Struct, StructType> {
  private boolean allocated = false;
  private long parentOffset = -1;
  private long offset;
  private StructType structType;
  private ByteArray bytes;
  private ByteArrayListener listener;

   public Struct(Struct struct) {
    this(struct.allocated, struct.parentOffset, struct.offset, struct.structType, struct.bytes.copy());
   }

  public Struct(StructType structType) {
    this(0, structType);
  }

  public Struct(StructType structType, ByteArray bytes) {
    this(true, -1, 0, structType, bytes);
  }

  public Struct(int offset, StructType structType) {
    this(false, -1, offset, structType, new ByteArray());
  }

  public Struct(long offset, StructType structType, ByteArray bytes) {
    this(true, -1, offset, structType, bytes);
  }

  public Struct(long parentOffset, long offset, StructType structType, ByteArray bytes) {
    this(true, parentOffset, offset, structType, bytes);
  }

  public Struct(boolean allocated, long parentOffset, long offset, StructType structType, ByteArray bytes) {
     this.allocated = allocated;
     this.parentOffset = parentOffset;
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

  public long getByteLength(int position) {
    return structType.getType(position).getByteLength(this);
  }

  public long getByteLength(int position, long index) {
     return ((ListType<?, ?, ?>) structType.getType(position)).getByteLength(this, index);
  }

  public long getByteLength(int position, long index, long length) {
     return ((ListType<?, ?, ?>) structType.getType(position)).getByteLength(this, index, length);
  }

  public long getArrayLength(int position) {
    return ((ListType<?, ?, ?>) structType.getType(position)).getLength(this);
  }

  @Override
  public void removeByteArrayListener() {
     bytes.removeListener(listener);
  }

  @Override
  public long getParentOffset() {
    return parentOffset;
  }

  public <T> T get(int position) {
    return ((ValueType<?, T>)structType.getType(position)).get(this);
  }

  public <T> Struct set(int position, T value) {
     ((ValueType<?, T>)structType.getType(position)).set(this, value);
     return this;
  }

  public <T> List<T> getStandardList(int position) {
    return ((ListType<?, T, ?>) structType.getType(position)).getList(this);
  }

  public <T> Struct set(int position, List<T> values) {
    ((ListType<?, T, ?>) structType.getType(position)).set(this, values);
    return this;
  }

  public <T> Struct set(int position, long index, List<T> values) {
    ((ListType<?, T, ?>) structType.getType(position)).set(this, index, values);
    return this;
  }

  public <L extends BinaryList<L, T, E>, T extends ListType<T, E, L>, E> L getList(int position) {
    return ((ListType<T, E, L>) structType.getType(position)).get(this);
  }

  public <L extends BinaryList<L, T, E>, T extends ListType<T, E, L>, E> L getList(int position, long index, long length) {
    return ((ListType<T, E, L>) structType.getType(position)).get(this, index, length);
  }

  public <L extends BinaryList<L, T, E>, T extends ListType<T, E, L>, E> Struct set(int position, L values) {
    ((ListType<T, E, L>) structType.getType(position)).set(this, values);
    return this;
  }

  public <L extends BinaryList<L, T, E>, T extends ListType<T, E, L>, E> Struct set(int position, long index, L values) {
    ((ListType<T, E, L>) structType.getType(position)).set(this, index, values);
    return this;
  }

  public <T> Struct add(int position, List<T> values) {
    ((ListType<?, T, ?>) structType.getType(position)).add(this, values);
    return this;
  }

  public <T> Struct add(int position, long index, List<T> values) {
    ((ListType<?, T, ?>) structType.getType(position)).add(this, index, values);
    return this;
  }

  public void remove(int position) {
    ((ValueType<?, ?>) structType.getType(position)).remove(this);
  }

  public void remove(int position, long index) {
    ((ListType<?, ?, ?>) structType.getType(position)).remove(this, index);
  }

  public void remove(int position, long index, long length) {
     ((ListType<?, ?, ?>) structType.getType(position)).remove(this, index, length);
  }

  public boolean getBool(int position) {
    return ((BoolType) structType.getType(position)).getBool(this);
  }

  public Struct set(int position, boolean b) {
    ((BoolType) structType.getType(position)).set(this, b);
    return this;
  }

  public boolean getBool(int position, long index) {
    return ((BoolListType) structType.getType(position)).getBool(this, index);
  }

  public Struct set(int position, long index, boolean b) {
    ((BoolListType) structType.getType(position)).set(this, index, b);
    return this;
  }

  public Struct setBool(int position, boolean... values) {
    ((BoolListType) structType.getType(position)).set(this, values);
    return this;
  }

  public boolean[] getBoolArray(int position) {
    return ((BoolListType) structType.getType(position)).getBoolArray(this);
  }
  
  public boolean[] getBoolArray(int position, long index, long length) {
    return ((BoolListType) structType.getType(position)).getBoolArray(this, index, length);
  }
  
  public Struct setBool(int position, long index, boolean... values) {
    ((BoolListType) structType.getType(position)).set(this, index, values);
    return this;
  }
  
  public Struct setBool(int position, List<Boolean> values) {
    ((BoolListType) structType.getType(position)).set(this, values);
    return this;
  }
  
  public Struct setBool(int position, long index, List<Boolean> values) {
    ((BoolListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addBool(int position, boolean b) {
    ((BoolListType) structType.getType(position)).add(this, b);
    return this;
  }

  public Struct bool(int position, boolean b) {
     return addBool(position, b);
  }

  public Struct addBool(int position, long index, boolean b) {
    ((BoolListType) structType.getType(position)).add(this, index, b);
    return this;
  }

  public Struct bool(int position, long index, boolean b) {
     return addBool(position, index, b);
  }

  public Struct addBool(int position, boolean... values) {
     ((BoolListType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct bool(int position, boolean... values) {
     return addBool(position, values);
  }

  public Struct addBool(int position, long index, boolean... values) {
     ((BoolListType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public Struct bool(int position, long index, boolean... values) {
     return addBool(position, index, values);
  }

  public Struct addBool(int position, List<Boolean> values) {
     ((BoolListType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct bool(int position, List<Boolean> values) {
     return addBool(position, values);
  }

  public Struct addBool(int position, long index, List<Boolean> values) {
     ((BoolListType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public Struct bool(int position, long index, List<Boolean> values) {
     return addBool(position, index, values);
  }

  public byte getInt8(int position) {
    return ((Int8Type) structType.getType(position)).getInt8(this);
  }

  public Struct setInt8(int position, byte b) {
    ((Int8Type) structType.getType(position)).set(this, b);
    return this;
  }

  public Struct setInt8(int position, int b) {
    return setInt8(position, (byte) b);
  }

  public byte getInt8(int position, long index) {
    return ((Int8ListType) structType.getType(position)).getInt8(this, index);
  }

  public Struct setInt8(int position, long index, byte b) {
    ((Int8ListType) structType.getType(position)).set(this, index, b);
    return this;
  }

  public Struct setInt8(int position, long index, int b) {
    return setInt8(position, index, (byte) b);
  }

  public byte[] getInt8Array(int position) {
    return ((Int8ListType) structType.getType(position)).getInt8Array(this);
  }

  public Struct setInt8(int position, byte... values) {
    ((Int8ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt8(int position, int... values) {
     return setInt8(position, mapIntsToBytes(values));
  }

  public byte[] getInt8Array(int position, long index, long length) {
    return ((Int8ListType) structType.getType(position)).getInt8Array(this, index, length);
  }

  public Struct setInt8(int position, long index, byte... values) {
    ((Int8ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setInt8(int position, long index, int... values) {
    return setInt8(position, index, mapIntsToBytes(values));
  }

  public Struct setInt8(int position, List<Byte> values) {
    ((Int8ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt8(int position, long index, List<Byte> values) {
    ((Int8ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addInt8(int position, byte b) {
    ((Int8ListType) structType.getType(position)).add(this, b);
    return this;
  }

  public Struct addInt8(int position, int b) {
    return addInt8(position, (byte) b);
  }

  public Struct int8(int position, int b) {
    return addInt8(position, b);
  }

  public Struct addInt8(int position, byte... values) {
    ((Int8ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct addInt8(int position, int... values) {
    return addInt8(position, mapIntsToBytes(values));
  }

  public Struct int8(int position, int... values) {
    return addInt8(position, values);
  }

  public Struct addInt8(int position, List<Byte> values) {
    ((Int8ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int8(int position, List<Byte> values) {
    return addInt8(position, values);
  }

  public Struct addInt8(int position, long index, byte b) {
    ((Int8ListType) structType.getType(position)).add(this, index, b);
    return this;
  }

  public Struct addInt8(int position, long index, int b) {
    return addInt8(position, index, (byte) b);
  }

  public Struct int8(int position, long index, int b) {
    return addInt8(position, index, b);
  }

  public Struct addInt8(int position, long index, byte... values) {
    ((Int8ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct addInt8(int position, long index, int... values) {
    return addInt8(position, index, mapIntsToBytes(values));
  }

  public Struct int8(int position, long index, int... values) {
    return addInt8(position, index, values);
  }

  public Struct addInt8(int position, long index, List<Byte> values) {
    ((Int8ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int8(int position, long index, List<Byte> values) {
    return addInt8(position, index, values);
  }

  public short getUint8(int position) {
    return ((Uint8Type) structType.getType(position)).getUint8(this);
  }

  public Struct setUint8(int position, short s) {
    ((Uint8Type) structType.getType(position)).set(this, s);
    return this;
  }

  public Struct setUint8(int position, int s) {
    return setUint8(position, (short) s);
  }

  public short getUint8(int position, long index) {
    return ((Uint8ListType) structType.getType(position)).getUint8(this, index);
  }

  public Struct setUint8(int position, long index, short s) {
    ((Uint8ListType) structType.getType(position)).set(this, index, s);
    return this;
  }

  public Struct setUint8(int position, long index, int s) {
     return setUint8(position, index, (short) s);
  }

  public short[] getUint8Array(int position) {
    return ((Uint8ListType) structType.getType(position)).getUint8Array(this);
  }

  public Struct setUint8(int position, short... values) {
    ((Uint8ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setUint8(int position, int... values) {
    return setUint8(position, mapIntsToShorts(values));
  }

  public short[] getUint8Array(int position, long index, long length) {
    return ((Uint8ListType) structType.getType(position)).getUint8Array(this, index, length);
  }

  public Struct setUint8(int position, long index, short... values) {
    ((Uint8ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setUint8(int position, long index, int... values) {
    return setInt8(position, index, mapIntsToBytes(values));
  }

  public Struct setUint8(int position, List<Short> values) {
    ((Uint8ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setUint8(int position, long index, List<Short> values) {
    ((Uint8ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addUint8(int position, short s) {
    ((Uint8ListType) structType.getType(position)).add(this, s);
    return this;
  }

  public Struct addUint8(int position, int s) {
     return addUint8(position, (short) s);
  }

  public Struct uint8(int position, int s) {
    return addUint8(position, s);
  }

  public Struct addUint8(int position, short... values) {
    ((Uint8ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct addUint8(int position, int... values) {
    return addUint8(position, mapIntsToShorts(values));
  }

  public Struct uint8(int position, int... values) {
    return addUint8(position, values);
  }

  public Struct addUint8(int position, List<Short> values) {
    ((Uint8ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct uint8(int position, List<Short> values) {
    return addUint8(position, values);
  }

  public Struct addUint8(int position, long index, short s) {
    ((Uint8ListType) structType.getType(position)).add(this, index, s);
    return this;
  }

  public Struct addUint8(int position, long index, int s) {
     return addUint8(position, index, (short) s);
  }

  public Struct uint8(int position, long index, int s) {
    return addUint8(position, index, s);
  }

  public Struct addUint8(int position, long index, short... values) {
    ((Uint8ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct addUint8(int position, long index, int... values) {
    return addUint8(position, index, mapIntsToShorts(values));
  }

  public Struct uint8(int position, long index, int... values) {
    return addUint8(position, index, values);
  }

  public Struct addUint8(int position, long index, List<Short> values) {
    ((Uint8ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct uint8(int position, long index, List<Short> values) {
    return addUint8(position, index, values);
  }

  public short getInt16(int position) {
    return ((Int16Type) structType.getType(position)).getInt16(this);
  }

  public Struct setInt16(int position, short s) {
    ((Int16Type) structType.getType(position)).set(this, s);
    return this;
  }

  public Struct setInt16(int position, int s) {
    return setInt16(position, (short) s);
  }

  public short getInt16(int position, long index) {
    return ((Int16ListType) structType.getType(position)).getInt16(this, index);
  }

  public Struct setInt16(int position, long index, short s) {
    ((Int16ListType) structType.getType(position)).set(this, index, s);
    return this;
  }

  public Struct setInt16(int position, long index, int s) {
     return setInt16(position, index, (short) s);
  }

  public short[] getInt16Array(int position) {
    return ((Int16ListType) structType.getType(position)).getInt16Array(this);
  }

  public Struct setInt16(int position, short... values) {
    ((Int16ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt16(int position, int... values) {
    return setInt16(position, mapIntsToShorts(values));
  }

  public short[] getInt16Array(int position, long index, long length) {
    return ((Int16ListType) structType.getType(position)).getInt16Array(this, index, length);
  }

  public Struct setInt16(int position, long index, short... values) {
    ((Int16ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setInt16(int position, long index, int... values) {
    return setInt16(position, index, mapIntsToShorts(values));
  }

  public Struct setInt16(int position, List<Short> values) {
    ((Int16ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt16(int position, long index, List<Short> values) {
    ((Int16ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addInt16(int position, short s) {
    ((Int16ListType) structType.getType(position)).add(this, s);
    return this;
  }

  public Struct addInt16(int position, int s) {
     return addInt16(position, (short) s);
  }

  public Struct int16(int position, int s) {
    return addInt16(position, s);
  }

  public Struct addInt16(int position, short... values) {
    ((Int16ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct addInt16(int position, int... values) {
    return addInt16(position, mapIntsToShorts(values));
  }

  public Struct int16(int position, int... values) {
    return addInt16(position, values);
  }

  public Struct addInt16(int position, List<Short> values) {
    ((Int16ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int16(int position, List<Short> values) {
    return addInt16(position, values);
  }

  public Struct addInt16(int position, long index, short s) {
    ((Int16ListType) structType.getType(position)).add(this, index, s);
    return this;
  }

  public Struct addInt16(int position, long index, int s) {
     return addInt16(position, index, (short) s);
  }

  public Struct int16(int position, long index, int s) {
    return addInt16(position, index, s);
  }

  public Struct addInt16(int position, long index, short... values) {
    ((Int16ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct addInt16(int position, long index, int... values) {
    return addInt16(position, index, mapIntsToShorts(values));
  }

  public Struct int16(int position, long index, int... values) {
    return addInt16(position, index, values);
  }

  public Struct addInt16(int position, long index, List<Short> values) {
    ((Int16ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int16(int position, long index, List<Short> values) {
    return addInt16(position, index, values);
  }

  public int getUint16(int position) {
    return ((Uint16Type) structType.getType(position)).getUint16(this);
  }

  public Struct setUint16(int position, int i) {
    ((Uint16Type) structType.getType(position)).set(this, i);
    return this;
  }

  public int getUint16(int position, long index) {
    return ((Uint16ListType) structType.getType(position)).getUint16(this, index);
  }

  public Struct setUint16(int position, long index, int i) {
    ((Uint16ListType) structType.getType(position)).set(this, index, i);
    return this;
  }

  public int[] getUint16Array(int position) {
    return ((Uint16ListType) structType.getType(position)).getUint16Array(this);
  }

  public Struct setUint16(int position, int... values) {
    ((Uint16ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public int[] getUint16Array(int position, long index, long length) {
    return ((Uint16ListType) structType.getType(position)).getUint16Array(this, index, length);
  }

  public Struct setUint16(int position, long index, int... values) {
    ((Uint16ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setUint16(int position, List<Integer> values) {
    ((Uint16ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setUint16(int position, long index, List<Integer> values) {
    ((Uint16ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addUint16(int position, int i) {
    ((Uint16ListType) structType.getType(position)).add(this, i);
    return this;
  }

  public Struct uint16(int position, int i) {
    return addUint16(position, i);
  }

  public Struct addUint16(int position, int... values) {
    ((Uint16ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct uint16(int position, int... values) {
    return addUint16(position, values);
  }

  public Struct addUint16(int position, List<Integer> values) {
    ((Uint16ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct uint16(int position, List<Integer> values) {
    return addUint16(position, values);
  }

  public Struct addUint16(int position, long index, int i) {
    ((Uint16ListType) structType.getType(position)).add(this, index, i);
    return this;
  }

  public Struct uint16(int position, long index, int i) {
    return addUint16(position, index, i);
  }

  public Struct addUint16(int position, long index, int... values) {
    ((Uint16ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct uint16(int position, long index, int... values) {
    return addUint16(position, index, values);
  }

  public Struct addUint16(int position, long index, List<Integer> values) {
    ((Uint16ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct uint16(int position, long index, List<Integer> values) {
    return addUint16(position, index, values);
  }

  public int getInt32(int position) {
    return ((Int32Type) structType.getType(position)).getInt32(this);
  }

  public Struct setInt32(int position, int i) {
    ((Int32Type) structType.getType(position)).set(this, i);
    return this;
  }

  public int getInt32(int position, long index) {
    return ((Int32ListType) structType.getType(position)).getInt32(this, index);
  }

  public Struct setInt32(int position, long index, int i) {
    ((Int32ListType) structType.getType(position)).set(this, index, i);
    return this;
  }

  public int[] getInt32Array(int position) {
    return ((Int32ListType) structType.getType(position)).getInt32Array(this);
  }

  public Struct setInt32(int position, int... values) {
    ((Int32ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public int[] getInt32Array(int position, long index, long length) {
    return ((Int32ListType) structType.getType(position)).getInt32Array(this, index, length);
  }

  public Struct setInt32(int position, long index, int... values) {
    ((Int32ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setInt32(int position, List<Integer> values) {
    ((Int32ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt32(int position, long index, List<Integer> values) {
    ((Int32ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addInt32(int position, int i) {
    ((Int32ListType) structType.getType(position)).add(this, i);
    return this;
  }

  public Struct addInt32(int position, int... values) {
    ((Int32ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int32(int position, int... values) {
    return addInt32(position, values);
  }

  public Struct addInt32(int position, List<Integer> values) {
    ((Int32ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int32(int position, List<Integer> values) {
    return addInt32(position, values);
  }

  public Struct addInt32(int position, long index, int i) {
    ((Int32ListType) structType.getType(position)).add(this, index, i);
    return this;
  }

  public Struct int32(int position, long index, int i) {
    return addInt32(position, index, i);
  }

  public Struct addInt32(int position, long index, int... values) {
    ((Int32ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int32(int position, long index, int... values) {
     return addInt32(position, index, values);
  }

  public Struct addInt32(int position, long index, List<Integer> values) {
    ((Int32ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int32(int position, long index, List<Integer> values) {
     return addInt32(position, index, values);
  }

  public long getUint32(int position) {
    return ((Uint32Type) structType.getType(position)).getUint32(this);
  }

  public Struct setUint32(int position, long l) {
    ((Uint32Type) structType.getType(position)).set(this, l);
    return this;
  }

  public long getUint32(int position, long index) {
    return ((Uint32ListType) structType.getType(position)).getUint32(this, index);
  }

  public Struct setUint32(int position, long index, long l) {
    ((Uint32ListType) structType.getType(position)).set(this, index, l);
    return this;
  }

  public long[] getUint32Array(int position) {
    return ((Uint32ListType) structType.getType(position)).getUint32Array(this);
  }

  public Struct setUint32(int position, long... values) {
    ((Uint32ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public long[] getUint32Array(int position, long index, long length) {
    return ((Uint32ListType) structType.getType(position)).getUint32Array(this, index, length);
  }

  public Struct setUint32(int position, long index, long... values) {
    ((Uint32ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setUint32(int position, List<Long> values) {
    ((Uint32ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setUint32(int position, long index, List<Long> values) {
    ((Uint32ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addUint32(int position, long l) {
    ((Uint32ListType) structType.getType(position)).add(this, l);
    return this;
  }

  public Struct uint32(int position, long l) {
     return addUint32(position, l);
  }

  public Struct addUint32(int position, long... values) {
    ((Uint32ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct uint32(int position, long... values) {
     return addUint32(position, values);
  }

  public Struct addUint32(int position, List<Long> values) {
    ((Uint32ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct uint32(int position, List<Long> values) {
     return addUint32(position, values);
  }

  public Struct addUint32(int position, long index, long l) {
    ((Uint32ListType) structType.getType(position)).add(this, index, l);
    return this;
  }

  public Struct uint32(int position, long index, long l) {
     return addUint32(position, index, l);
  }

  public Struct addUint32(int position, long index, long... values) {
    ((Uint32ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct uint32(int position, long index, long... values) {
     return addUint32(position, index, values);
  }

  public Struct addUint32(int position, long index, List<Long> values) {
    ((Uint32ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct uint32(int position, long index, List<Long> values) {
     return addUint32(position, index, values);
  }

  public long getInt64(int position) {
    return ((Int64Type) structType.getType(position)).getInt64(this);
  }

  public Struct setInt64(int position, long l) {
    ((Int64Type) structType.getType(position)).set(this, l);
    return this;
  }

  public long getInt64(int position, long index) {
    return ((Int64ListType) structType.getType(position)).getInt64(this, index);
  }

  public Struct setInt64(int position, long index, long l) {
    ((Int64ListType) structType.getType(position)).set(this, index, l);
    return this;
  }

  public long[] getInt64Array(int position) {
    return ((Int64ListType) structType.getType(position)).getInt64Array(this);
  }

  public Struct setInt64(int position, long... values) {
    ((Int64ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public long[] getInt64Array(int position, long index, long length) {
    return ((Int64ListType) structType.getType(position)).getInt64Array(this, index, length);
  }

  public Struct setInt64(int position, long index, long... values) {
    ((Int64ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setInt64(int position, List<Long> values) {
    ((Int64ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct setInt64(int position, long index, List<Long> values) {
    ((Int64ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct addInt64(int position, long l) {
    ((Int64ListType) structType.getType(position)).add(this, l);
    return this;
  }

  public Struct int64(int position, long l) {
     return addInt64(position, l);
  }

  public Struct addInt64(int position, long... values) {
    ((Int64ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int64(int position, long... values) {
     return addInt64(position, values);
  }

  public Struct addInt64(int position, List<Long> values) {
    ((Int64ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct int64(int position, List<Long> values) {
     return addInt64(position, values);
  }

  public Struct addInt64(int position, long index, long l) {
    ((Int64ListType) structType.getType(position)).add(this, index, l);
    return this;
  }

  public Struct int64(int position, long index, long l) {
     return addInt64(position, index, l);
  }

  public Struct addInt64(int position, long index, long... values) {
    ((Int64ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int64(int position, long index, long... values) {
     return addInt64(position, index, values);
  }

  public Struct addInt64(int position, long index, List<Long> values) {
    ((Int64ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct int64(int position, long index, List<Long> values) {
     return addInt64(position, index, values);
  }

  public float getFloat32(int position) {
    return ((Float32Type) structType.getType(position)).getFloat32(this);
  }

  public Struct setFloat32(int position, float f) {
    ((Float32Type) structType.getType(position)).set(this, f);
    return this;
  }

  public float getFloat32(int position, long index) {
    return ((Float32ListType) structType.getType(position)).getFloat32(this, index);
  }

  public Struct setFloat32(int position, long index, float f) {
    ((Float32ListType) structType.getType(position)).set(this, index, f);
    return this;
  }

  public Float32List getFloat32Array(int position) {
    return ((Float32ListType) structType.getType(position)).get(this);
  }

  public Struct setFloat32(int position, float... values) {
     ((Float32ListType) structType.getType(position)).set(this, values);
     return this;
  }

  public float[] getFloat32Array(int position, long index, long length) {
    return ((Float32ListType) structType.getType(position)).getFloat32Array(this, index, length);
  }

  public Struct setFloat32(int position, long index, float... values) {
    ((Float32ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setFloat32(int position, List<Float> values) {
     ((Float32ListType) structType.getType(position)).set(this, values);
     return this;
  }

  public Struct addFloat32(int position, float f) {
    ((Float32ListType) structType.getType(position)).add(this, f);
    return this;
  }

  public Struct addFloat32(int position, long index, float f) {
    ((Float32ListType) structType.getType(position)).add(this, index, f);
    return this;
  }

  public Struct addFloat32(int position, float... values) {
     ((Float32ListType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct addFloat32(int position, long index, float... values) {
     ((Float32ListType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public Struct addFloat32(int position, List<Float> values) {
     ((Float32ListType) structType.getType(position)).add(this, values);
     return this;
  }

  public Struct addFloat32(int position, long index, List<Float> values) {
     ((Float32ListType) structType.getType(position)).add(this, index, values);
     return this;
  }

  public double getFloat64(int position) {
    return ((Float64Type) structType.getType(position)).getFloat64(this);
  }

  public double getFloat64(int position, long index) {
    return ((Float64ListType) structType.getType(position)).getFloat64(this, index);
  }

  public Struct setFloat64(int position, double d) {
    ((Float64Type) structType.getType(position)).set(this, d);
    return this;
  }

  public Struct setFloat64(int position, long index, double d) {
    ((Float64ListType) structType.getType(position)).set(this, index, d);
    return this;
  }

  public Struct addFloat64(int position, double d) {
    ((Float64ListType) structType.getType(position)).add(this, d);
    return this;
  }

  public Struct addFloat64(int position, long index, double d) {
    ((Float64ListType) structType.getType(position)).add(this, index, d);
    return this;
  }

  public Float64List getFloat64Array(int position) {
    return ((Float64ListType) structType.getType(position)).get(this);
  }

  public double[] getFloat64RawArray(int position) {
    return ((Float64ListType) structType.getType(position)).getFloat64Array(this);
  }

  public Struct setFloat64(int position, double... values) {
    ((Float64ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public double[] getFloat64Array(int position, long index, long length) {
    return ((Float64ListType) structType.getType(position)).getFloat64Array(this, index, length);
  }

  public Struct setFloat64(int position, long index, double... values) {
    ((Float64ListType) structType.getType(position)).set(this, index, values);
    return this;
  }

  public Struct setFloat64(int position, List<Double> values) {
    ((Float64ListType) structType.getType(position)).set(this, values);
    return this;
  }

  public Struct addFloat64(int position, double... values) {
    ((Float64ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct addFloat64(int position, long index, double... values) {
    ((Float64ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  public Struct addFloat64(int position, List<Double> values) {
    ((Float64ListType) structType.getType(position)).add(this, values);
    return this;
  }

  public Struct addFloat64(int position, long index, List<Double> values) {
    ((Float64ListType) structType.getType(position)).add(this, index, values);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Struct struct)) return false;

    return structType.equals(struct.structType) && getByteArray().compareBytes(getOffset(), struct.getByteArray(), struct.getOffset(), getByteLength());
  }

  @Override
  public int hashCode() {
    int result = structType.hashCode();
    var bytes = getByteArray().getInt8(getOffset(), getByteLength());
    for (int i = 0; i < bytes.length; i++) {
      result = 31 * result + bytes[i];
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    for (int i = 0; i < getPositions(); i++) {
      if (i > 0) builder.append(", ");
      Type<?> type = getType(i);
      if (type instanceof BasicType<?,?> primitiveType) {
        builder.append(primitiveType.getTypeInfo().label()).append("=");
      }
      if (type instanceof BoolType) {
        builder.append(getBool(i));
      } else if (type instanceof Int8Type) {
        builder.append(getInt8(i));
      } else if (type instanceof Uint8Type) {
        builder.append(getUint8(i));
      } else if (type instanceof Int16Type) {
        builder.append(getInt16(i));
      } else if (type instanceof Uint16Type) {
        builder.append(getUint16(i));
      } else if (type instanceof Int32Type) {
        builder.append(getInt32(i));
      } else if (type instanceof Uint32Type) {
        builder.append(getUint32(i));
      } else if (type instanceof Int64Type) {
        builder.append(getInt64(i));
      } else if (type instanceof Uint64Type) {
        builder.append(get(i).toString());
      } else if (type instanceof Float32Type) {
        builder.append(getFloat32(i));
      } else if (type instanceof Float64Type) {
        builder.append(getFloat64(i));
      } else if (type instanceof StructType) {
        builder.append("Struct=").append(get(i).toString());
      } else if (type instanceof PadType padType) {
        if (padType.isAlign()) {
          builder.append("align=").append(padType.getByteLength(this));
        } else {
          builder.append("pad=").append(padType.getByteLength(this));
        }
      }
    }
    builder.append("}");
    return builder.toString();
  }
}
