package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.BooleanConsumer;
import com.github.moaxcp.verybinary.jdk.BooleanIterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.math.Expression.constant;

public final class BoolList extends PrimitiveList<BoolList, BoolListType, Boolean> implements PrimitiveIterable<Boolean, BooleanConsumer> {

  public static BoolList toBoolList(boolean[] values) {
    return getBoolListStruct(values)
        .getBoolList(0);
  }

  public static Struct getBoolListStruct(boolean[] values) {
    return struct(getBoolListStructType(values.length))
        .build()
        .setBool(0, values);
  }

  public static Struct getBoolListStruct(List<Boolean> values) {
    return struct(getBoolListStructType(values.size()))
        .build()
        .setBool(0, values);
  }

  public static StructType getBoolListStructType(long length) {
    return structType()
        .boolArray(constant(length))
        .build();
  }

  public BoolList(Pointer<?,? extends Type<?>> pointer, BoolListType type) {
    super(pointer, type, 0, 0, false);
  }

  public BoolList(Pointer<?, ? extends Type<?>> pointer, BoolListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public boolean getBool(long index) {
    return ((BoolListType) type).getBool(pointer, getIndex(index));
  }

  public void set(long index, boolean value) {
    ((BoolListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, boolean... values) {
    ((BoolListType) type).set(pointer, index, values);
  }

  @Override
  public BooleanIterator iterator() {
    return new BoolArrayIterator();
  }

  private class BoolArrayIterator implements BooleanIterator {
    private long index = 0;

    @Override
    public boolean nextBoolean() {
      return ((BoolListType) type).getBool(pointer, getIndex(index++));
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

  @Override
  public BoolList copy() {
    var s = struct(getBoolListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getLength(pointer));
    return s.getBoolList(0);
  }
  
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoolList that = (BoolList) o;
    if (size64() != that.size64()) {
      return false;
    }
    BooleanIterator thisIter = this.iterator();
    BooleanIterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextBoolean() != thatIter.nextBoolean()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    BooleanIterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Boolean.hashCode(iter.nextBoolean());
    }
    return result;
  }
}
