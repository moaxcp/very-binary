package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Int8Consumer;
import com.github.moaxcp.verybinary.jdk.Int8Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.math.Expression.constant;

public final class Int8List extends PrimitiveList<Int8List, Int8ListType, Byte> implements PrimitiveIterable<Byte, Int8Consumer> {

  public static Int8List toInt8List(byte[] values) {
    return getInt8ListStruct(values)
        .getInt8List(0);
  }

  public static Struct getInt8ListStruct(byte[] values) {
    return struct(getInt8ListStructType(values.length))
        .build()
        .setInt8(0, values);
  }

  public static Struct getInt8ListStruct(List<Byte> values) {
    return struct(getInt8ListStructType(values.size()))
        .build()
        .setInt8(0, values);
  }

  public static StructType getInt8ListStructType(long length) {
    return structType()
        .int8Array(constant(length))
        .build();
  }

  public Int8List(Pointer<?,? extends Type<?>> pointer, Int8ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Int8List(Pointer<?, ? extends Type<?>> pointer, Int8ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public byte getInt8(long index) {
    return ((Int8ListType) type).getInt8(pointer, getIndex(index));
  }

  public void set(long index, byte value) {
    ((Int8ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, byte... values) {
    ((Int8ListType) type).set(pointer, index, values);
  }

  @Override
  public Int8Iterator iterator() {
    return new Int8ArrayIterator();
  }

  private class Int8ArrayIterator implements Int8Iterator {
    private long index = 0;

    @Override
    public byte nextInt8() {
      return ((Int8ListType) type).getInt8(pointer, getIndex(index++));
    }

    @Override
    public boolean hasNext() {
      return index < (checkLength ? length : type.getLength(pointer));
    }

    @Override
    public void remove() {
      type.remove(pointer, getIndex(index--));
    }
  }

  public Int8List copy() {
    var s = struct(getInt8ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getInt8List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Int8List that = (Int8List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Int8Iterator thisIter = this.iterator();
    Int8Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextInt8() != thatIter.nextInt8()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Int8Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Byte.hashCode(iter.nextInt8());
    }
    return result;
  }
}
