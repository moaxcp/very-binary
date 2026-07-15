package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Int16Consumer;
import com.github.moaxcp.verybinary.jdk.Int16Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.math.Expression.constant;

public final class Int16List extends PrimitiveList<Int16List, Int16ListType, Short> implements PrimitiveIterable<Short, Int16Consumer> {

  public static Int16List toInt16List(short[] values) {
    return getInt16ListStruct(values)
        .getInt16List(0);
  }

  public static Struct getInt16ListStruct(short[] values) {
    return struct(getInt16ListStructType(values.length))
        .build()
        .setInt16(0, values);
  }

  public static Struct getInt16ListStruct(List<Short> values) {
    return struct(getInt16ListStructType(values.size()))
        .build()
        .setInt16(0, values);
  }

  public static StructType getInt16ListStructType(long length) {
    return structType()
        .int16Array(constant(length))
        .build();
  }

  public Int16List(Pointer<?,? extends Type<?>> pointer, Int16ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Int16List(Pointer<?, ? extends Type<?>> pointer, Int16ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public short getInt16(long index) {
    return ((Int16ListType) type).getInt16(pointer, getIndex(index));
  }

  public void set(long index, short value) {
    ((Int16ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, short... values) {
    ((Int16ListType) type).set(pointer, index, values);
  }

  @Override
  public Int16Iterator iterator() {
    return new Int16ArrayIterator();
  }

  private class Int16ArrayIterator implements Int16Iterator {
    private long index = 0;

    @Override
    public short nextInt16() {
      return ((Int16ListType) type).getInt16(pointer, getIndex(index++));
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

  public Int16List copy() {
    var s = struct(getInt16ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getInt16List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Int16List that = (Int16List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Int16Iterator thisIter = this.iterator();
    Int16Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextInt16() != thatIter.nextInt16()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Int16Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Short.hashCode(iter.nextInt16());
    }
    return result;
  }
}
