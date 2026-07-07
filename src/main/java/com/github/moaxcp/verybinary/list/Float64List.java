package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Float64Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;
import java.util.function.DoubleConsumer;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Float64List extends PrimitiveList<Float64List, Float64ListType, Double> implements PrimitiveIterable<Double, DoubleConsumer> {

  public static Float64List toFloat64List(double[] values) {
    return getFloat64ListStruct(values)
        .getFloat64List(0);
  }

  public static Struct getFloat64ListStruct(double[] values) {
    return struct(getFloat64ListStructType(values.length))
        .build()
        .setFloat64(0, values);
  }

  public static Struct getFloat64ListStruct(List<Double> values) {
    return struct(getFloat64ListStructType(values.size()))
        .build()
        .setFloat64(0, values);
  }

  public static StructType getFloat64ListStructType(long length) {
    return structType()
        .float64Array(constant(length))
        .build();
  }

  public Float64List(Pointer<?,? extends Type<?>> pointer, Float64ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Float64List(Pointer<?, ? extends Type<?>> pointer, Float64ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public double getFloat64(long index) {
    return ((Float64ListType) type).getFloat64(pointer, getIndex(index));
  }

  public void set(long index, double value) {
    ((Float64ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, double... values) {
    ((Float64ListType) type).set(pointer, index, values);
  }

  @Override
  public Float64Iterator iterator() {
    return new Float64ArrayIterator();
  }

  private class Float64ArrayIterator implements Float64Iterator {
    private long index = 0;

    @Override
    public double nextFloat64() {
      return ((Float64ListType) type).getFloat64(pointer, getIndex(index++));
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
  public Float64List copy() {
    var s = struct(getFloat64ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getFloat64List(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Float64List that = (Float64List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Float64Iterator thisIter = this.iterator();
    Float64Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextFloat64() != thatIter.nextFloat64()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Float64Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Double.hashCode(iter.nextFloat64());
    }
    return result;
  }
}
