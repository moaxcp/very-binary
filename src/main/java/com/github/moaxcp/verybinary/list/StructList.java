package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;

import java.util.Iterator;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class StructList extends BinaryList<StructList, StructListType, Struct> {

  public static StructType getStructListStructType(long length, StructType type) {
    return structType()
        .structArray(constant(length), type)
        .build();
  }

  public StructList(Pointer<?, ? extends Type<?>> pointer, StructListType type) {
    super(pointer, type, 0, 0, false);
  }

  public StructList(Pointer<?, ? extends Type<?>> pointer, StructListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public Pointer<?, ? extends Type<?>> getPointer() {
    return pointer;
  }

  @Override
  public Iterator<Struct> iterator() {
    return new StructListIterator();
  }
  public int size() {
    return Math.toIntExact(length);
  }

  public long size64() {
    return length;
  }

  private class StructListIterator implements Iterator<Struct> {
    private long index = indexOffset;

    @Override
    public boolean hasNext() {
      return index < length;
    }

    @Override
    public Struct next() {
      return type.get(pointer, index++);
    }

    @Override
    public void remove() {
      type.remove(pointer, index--);
    }
  }

  @Override
  public StructList copy() {
    var s = struct(getStructListStructType(size64(), type.getStructType())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getLength(pointer));
    return s.getList(0);
  }
}
