package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Int32Consumer;
import com.github.moaxcp.verybinary.jdk.Int32Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Int32List extends PrimitiveList<Int32List, Int32ListType, Integer> implements PrimitiveIterable<Integer, Int32Consumer> {

  public static Int32List toInt32List(int[] values) {
    return getInt32ListStruct(values)
        .getList(0);
  }

  public static Struct getInt32ListStruct(int[] values) {
    return struct(getInt32ListStructType(values.length))
        .build()
        .setInt32(0, values);
  }

  public static Struct getInt32ListStruct(List<Integer> values) {
    return struct(getInt32ListStructType(values.size()))
        .build()
        .setInt32(0, values);
  }

  public static StructType getInt32ListStructType(long length) {
    return structType()
        .int32Array(constant(length))
        .build();
  }

  public Int32List(Pointer<?,? extends Type<?>> pointer, Int32ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Int32List(Pointer<?, ? extends Type<?>> pointer, Int32ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public int getInt32(long index) {
    return ((Int32ListType) type).getInt32(pointer, getIndex(index));
  }

  public void set(long index, int value) {
    ((Int32ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, int... values) {
    ((Int32ListType) type).set(pointer, index, values);
  }

  @Override
  public Int32Iterator iterator() {
    return new Int32ArrayIterator();
  }

  private class Int32ArrayIterator implements Int32Iterator {
    private long index = 0;

    @Override
    public int nextInt32() {
      return ((Int32ListType) type).getInt32(pointer, getIndex(index++));
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

  public Int32List copy() {
    var s = struct(getInt32ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getList(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Int32List that = (Int32List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Int32Iterator thisIter = this.iterator();
    Int32Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextInt32() != thatIter.nextInt32()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Int32Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Integer.hashCode(iter.nextInt32());
    }
    return result;
  }
}
