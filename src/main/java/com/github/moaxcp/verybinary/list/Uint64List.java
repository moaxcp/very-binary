package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Uint64List extends BinaryList<BigInteger> implements Iterable<BigInteger> {

  public static Uint64List toUint64List(long[] values) {
    return getUint64ListStruct(Arrays.stream(values).mapToObj(BigInteger::valueOf).collect(Collectors.toList()))
        .getUint64List(0);
  }

  public static Struct getUint64ListStruct(List<BigInteger> values) {
    return struct(getUint64ListStructType(values.size()))
        .build()
        .setUint64(0, values);
  }

  public static StructType getUint64ListStructType(long length) {
    return structType()
        .uint64Array(constant(length))
        .build();
  }

  public Uint64List(Pointer<?,? extends Type<?>> pointer, Uint64ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Uint64List(Pointer<?, ? extends Type<?>> pointer, Uint64ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  @Override
  public void set(long index, BigInteger value) {
    ((Uint64ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, BigInteger... values) {
    ((Uint64ListType) type).set(pointer, index, values);
  }

  @Override
  public Iterator iterator() {
    return new Uint64ArrayIterator();
  }

  private class Uint64ArrayIterator implements Iterator<BigInteger> {
    private long index = 0;

    @Override
    public BigInteger next() {
      return ((Uint64ListType) type).getUint64(pointer, getIndex(index++));
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

  public Uint64List copy() {
    var s = struct(getUint64ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getUint64List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Uint64List that = (Uint64List) o;
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
