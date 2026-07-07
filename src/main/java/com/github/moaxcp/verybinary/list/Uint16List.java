package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Uint16Consumer;
import com.github.moaxcp.verybinary.jdk.Uint16Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Uint16List extends PrimitiveList<Uint16List, Uint16ListType, Integer> implements PrimitiveIterable<Integer, Uint16Consumer> {

  public static Uint16List toUint16List(int[] values) {
    return getUint16ListStruct(values)
        .getUint16List(0);
  }

  public static Struct getUint16ListStruct(int[] values) {
    return struct(getUint16ListStructType(values.length))
        .build()
        .setUint16(0, values);
  }

  public static Struct getUint16ListStruct(List<Integer> values) {
    return struct(getUint16ListStructType(values.size()))
        .build()
        .setUint16(0, values);
  }

  public static StructType getUint16ListStructType(long length) {
    return structType()
        .uint16Array(constant(length))
        .build();
  }

  public Uint16List(Pointer<?,? extends Type<?>> pointer, Uint16ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Uint16List(Pointer<?, ? extends Type<?>> pointer, Uint16ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public int getUint16(long index) {
    return ((Uint16ListType) type).getUint16(pointer, getIndex(index));
  }

  public void set(long index, int value) {
    ((Uint16ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, int... values) {
    ((Uint16ListType) type).set(pointer, index, values);
  }

  @Override
  public Uint16Iterator iterator() {
    return new Uint16ArrayIterator();
  }

  private class Uint16ArrayIterator implements Uint16Iterator {
    private long index = 0;

    @Override
    public int nextUint16() {
      return ((Uint16ListType) type).getUint16(pointer, getIndex(index++));
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

  public Uint16List copy() {
    var s = struct(getUint16ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getUint16List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uint16List that = (Uint16List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Uint16Iterator thisIter = this.iterator();
    Uint16Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextUint16() != thatIter.nextUint16()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Uint16Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Integer.hashCode(iter.nextUint16());
    }
    return result;
  }
}
