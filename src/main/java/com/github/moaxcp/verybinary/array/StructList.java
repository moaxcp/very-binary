package com.github.moaxcp.verybinary.array;

import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Struct;
import com.github.moaxcp.verybinary.StructListType;
import com.github.moaxcp.verybinary.Type;

import java.util.AbstractList;
import java.util.Iterator;

public final class StructList extends AbstractList<Struct> {

  private final Pointer<?, ? extends Type<?>> pointer;
  private final StructListType type;
  private final long indexOffset;
  private final long length;

  public StructList(Pointer<?, ? extends Type<?>> pointer, StructListType type, long indexOffset, long length) {
    this.pointer = pointer;
    this.type = type;
    this.indexOffset = indexOffset;
    this.length = length;
  }

  public Pointer<?, ? extends Type<?>> getPointer() {
    return pointer;
  }

  @Override
  public Struct get(int index) {
    if (index > length) {
      throw new IndexOutOfBoundsException("Index out of range: " + index);
    }
    return type.get(pointer, index + indexOffset);
  }

  public Struct getStruct(long index) {
    if (index > length) {
      throw new IndexOutOfBoundsException("Index out of range: " + index);
    }
    return type.get(pointer, index + indexOffset);
  }

  @Override
  public Iterator<Struct> iterator() {
    return new StructListIterator();
  }

  @Override
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
}
