package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Uint32Consumer;
import com.github.moaxcp.verybinary.jdk.Uint32Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Uint32List extends PrimitiveList<Uint32List, Uint32ListType, Long> implements PrimitiveIterable<Long, Uint32Consumer> {

  public static Uint32List toUint32List(long[] values) {
    return getUint32ListStruct(values)
        .getList(0);
  }

  public static Struct getUint32ListStruct(long[] values) {
    return struct(getUint32ListStructType(values.length))
        .build()
        .setUint32(0, values);
  }

  public static Struct getUint32ListStruct(List<Long> values) {
    return struct(getUint32ListStructType(values.size()))
        .build()
        .setUint32(0, values);
  }

  public static StructType getUint32ListStructType(long length) {
    return structType()
        .uint32Array(constant(length))
        .build();
  }

  public Uint32List(Pointer<?,? extends Type<?>> pointer, Uint32ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Uint32List(Pointer<?, ? extends Type<?>> pointer, Uint32ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public long getUint32(long index) {
    return ((Uint32ListType) type).getUint32(pointer, getIndex(index));
  }

  public void set(long index, long value) {
    ((Uint32ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, long... values) {
    ((Uint32ListType) type).set(pointer, index, values);
  }

  @Override
  public Uint32Iterator iterator() {
    return new Uint32ArrayIterator();
  }

  private class Uint32ArrayIterator implements Uint32Iterator {
    private long index = 0;

    @Override
    public long nextUint32() {
      return ((Uint32ListType) type).getUint32(pointer, getIndex(index++));
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

  public Uint32List copy() {
    var s = struct(getUint32ListStructType(size64())).build();
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
    Uint32List that = (Uint32List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Uint32Iterator thisIter = this.iterator();
    Uint32Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextUint32() != thatIter.nextUint32()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Uint32Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Long.hashCode(iter.nextUint32());
    }
    return result;
  }
}
