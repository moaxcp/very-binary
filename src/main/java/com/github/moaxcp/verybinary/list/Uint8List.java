package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Uint8Consumer;
import com.github.moaxcp.verybinary.jdk.Uint8Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Uint8List extends PrimitiveList<Uint8List, Uint8ListType, Short> implements PrimitiveIterable<Short, Uint8Consumer> {

  public static Uint8List toUint8List(short[] values) {
    return getUint8ListStruct(values)
        .getUint8List(0);
  }

  public static Struct getUint8ListStruct(short[] values) {
    return struct(getUint8ListStructType(values.length))
        .build()
        .setUint8(0, values);
  }

  public static Struct getUint8ListStruct(List<Short> values) {
    return struct(getUint8ListStructType(values.size()))
        .build()
        .setUint8(0, values);
  }

  public static StructType getUint8ListStructType(long length) {
    return structType()
        .uint8Array(constant(length))
        .build();
  }

  public Uint8List(Pointer<?,? extends Type<?>> pointer, Uint8ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Uint8List(Pointer<?, ? extends Type<?>> pointer, Uint8ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public short getUint8(long index) {
    return ((Uint8ListType) type).getUint8(pointer, getIndex(index));
  }

  public void set(long index, short value) {
    ((Uint8ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, short... values) {
    ((Uint8ListType) type).set(pointer, index, values);
  }

  @Override
  public Uint8Iterator iterator() {
    return new Uint8ArrayIterator();
  }

  private class Uint8ArrayIterator implements Uint8Iterator {
    private long index = 0;

    @Override
    public short nextUint8() {
      return ((Uint8ListType) type).getUint8(pointer, getIndex(index++));
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

  public Uint8List copy() {
    var s = struct(getUint8ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getUint8List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uint8List that = (Uint8List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Uint8Iterator thisIter = this.iterator();
    Uint8Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextUint8() != thatIter.nextUint8()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Uint8Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Short.hashCode(iter.nextUint8());
    }
    return result;
  }
}
