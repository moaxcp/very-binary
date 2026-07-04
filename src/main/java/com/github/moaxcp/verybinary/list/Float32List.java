package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.*;
import com.github.moaxcp.verybinary.jdk.Float32Consumer;
import com.github.moaxcp.verybinary.jdk.Float32Iterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;

public final class Float32List extends PrimitiveList<Float32List, Float32ListType, Float> implements PrimitiveIterable<Float, Float32Consumer> {

  public static Float32List toFloat32List(float[] values) {
    return getFloat32ListStruct(values)
        .getFloat32Array(0);
  }

  public static Struct getFloat32ListStruct(float[] values) {
    return struct(getFloat32ListStructType(values.length))
        .build()
        .setFloat32(0, values);
  }

  public static Struct getFloat32ListStruct(List<Float> values) {
    return struct(getFloat32ListStructType(values.size()))
        .build()
        .setFloat32(0, values);
  }

  public static StructType getFloat32ListStructType(long length) {
    return structType()
        .float32Array(constant(length))
        .build();
  }

  public Float32List(Pointer<?,? extends Type<?>> pointer, Float32ListType type) {
    super(pointer, type, 0, 0, false);
  }

  public Float32List(Pointer<?, ? extends Type<?>> pointer, Float32ListType type, long indexOffset, long length) {
    super(pointer, type , indexOffset, length, true);
  }

  public float getFloat32(long index) {
    return ((Float32ListType) type).getFloat32(pointer, getIndex(index));
  }

  public void set(long index, float value) {
    ((Float32ListType) type).set(pointer, getIndex(index), value);
  }

  public void set(long index, float... values) {
    ((Float32ListType) type).set(pointer, index, values);
  }

  @Override
  public Float32Iterator iterator() {
    return new Float32ArrayIterator();
  }

  private class Float32ArrayIterator implements Float32Iterator {
    private long index = 0;

    @Override
    public float nextFloat() {
      return ((Float32ListType) type).getFloat32(pointer, getIndex(index++));
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
  public Float32List copy() {
    var s = struct(getFloat32ListStructType(size64())).build();
    s.getByteArray().setBytes(pointer.getByteArray(), type.getOffset(pointer), 0, type.getByteLength(pointer));
    return s.getFloat32Array(0);
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Float32List that = (Float32List) o;
    if (size64() != that.size64()) {
      return false;
    }
    Float32Iterator thisIter = this.iterator();
    Float32Iterator thatIter = that.iterator();
    while (thisIter.hasNext() && thatIter.hasNext()) {
      if (thisIter.nextFloat() != thatIter.nextFloat()) {
        return false;
      }
    }
    return true;
  }

  public int hashCode() {
    int result = 1;
    Float32Iterator iter = iterator();
    while (iter.hasNext()) {
      result = 31 * result + Float.hashCode(iter.nextFloat());
    }
    return result;
  }
}
