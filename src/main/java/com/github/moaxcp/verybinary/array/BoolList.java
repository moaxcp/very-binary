package com.github.moaxcp.verybinary.array;

import com.github.moaxcp.verybinary.BoolArrayType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;
import com.github.moaxcp.verybinary.jdk.BooleanConsumer;
import com.github.moaxcp.verybinary.jdk.BooleanIterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

import java.util.AbstractList;

public final class BoolList extends AbstractList<Boolean> implements PrimitiveIterable<Boolean, BooleanConsumer> {

  private final Pointer<?,? extends Type<?>> pointer;
  private final BoolArrayType type;

  public BoolList(Pointer<?,? extends Type<?>> pointer, BoolArrayType type) {
    this.pointer = pointer;
    this.type = type;
  }

  @Override
  public Boolean get(int index) {
    return type.getBool(pointer, index);
  }

  public boolean getBool(long index) {
    return type.getBool(pointer, index);
  }

  @Override
  public BooleanIterator iterator() {
    return new BoolArrayIterator();
  }

  @Override
  public int size() {
    return Math.toIntExact(type.getLength(pointer));
  }

  public long size64() {
    return type.getLength(pointer);
  }

  private class BoolArrayIterator implements BooleanIterator {
    private int index = 0;

    @Override
    public boolean nextBoolean() {
      return type.getBool(pointer, index++);
    }

    @Override
    public boolean hasNext() {
      return index < type.getLength(pointer);
    }

    @Override
    public void remove() {
      type.remove(pointer, index--);
    }
  }
}
