package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;

import java.util.Iterator;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;

public final class StructList extends BinaryList<StructList, StructListType, Struct> {

  public static StructType getStructListStructType(long length, StructType type) {
    return structType()
        .structList(int64Value(length), type)
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
    return s.getStructList(0);
  }

  @Override
  public String toString() {
    if (size64() == 0) {
      return "";
    }
    var first = get(0);
    var builder = new StringBuilder().append(first);
    for (long i = 1; i < size64(); i++) {
      builder.append(", ").append(get(i));
    }
    return builder.toString();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StructList that = (StructList) o;
    if (size64() != that.size64()) {
      return false;
    }
    var thisIter = this.iterator();
    var thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (!thisIter.next().equals(thatIter.next())) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    var iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + iter.next().hashCode();
    }
    return result;
  }
}
