package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Int64Consumer;
import com.github.moaxcp.verybinary.jdk.Int64Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Int64List extends PrimitiveList<Int64List, Int64ListType, Long> implements PrimitiveIterable<Long, Int64Consumer> {

  public static Int64List toInt64List(long[] values) {
    return getInt64ListStruct(values)
        .getInt64List(0);
  }

  public static Struct getInt64ListStruct(long[] values) {
    return struct(getInt64ListStructType(values.length))
        .build()
        .setInt64(0, values);
  }

  public static Struct getInt64ListStruct(List<Long> values) {
    return struct(getInt64ListStructType(values.size()))
        .build()
        .setInt64(0, values);
  }

  public static StructType getInt64ListStructType(long length) {
    return structType()
        .int64Array(constant(length))
        .build();
  }

  public Int64List(Pointer<?,? extends Type<?>> pointer, Int64ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Int64List(Pointer<?, ? extends Type<?>> pointer, Int64ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public long getInt64(long index) {
    return ((Int64ListType) type).getInt64(pointer, getIndex(index));
  }

  public void set(long index, long value) {
    ((Int64ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, long... values) {
    ((Int64ListType) type).set(pointer, index, values);
  }

  @Override
  public Int64Iterator iterator() {
    return new Int64ArrayIterator();
  }

  private class Int64ArrayIterator implements Int64Iterator {
    private long index = 0;

    @Override
    public long nextInt64() {
      return ((Int64ListType) type).getInt64(pointer, getIndex(index++));
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

  public Int64List copy() {
    var s = struct(getInt64ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getInt64List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Int64List that = (Int64List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Int64Iterator thisIter = this.iterator();
    Int64Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextInt64() != thatIter.nextInt64()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Int64Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Long.hashCode(iter.nextInt64());
    }
    return result;
  }
}
