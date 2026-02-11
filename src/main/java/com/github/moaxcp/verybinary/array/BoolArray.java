package com.github.moaxcp.verybinary.array;

import com.github.moaxcp.verybinary.BoolArrayType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;
import com.github.moaxcp.verybinary.jdk.BooleanConsumer;
import com.github.moaxcp.verybinary.jdk.BooleanIterator;
import com.github.moaxcp.verybinary.jdk.PrimitiveIterable;

public final class BoolArray implements PrimitiveIterable<Boolean, BooleanConsumer> {

  private final Pointer<?,? extends Type<?>> pointer;
  private final BoolArrayType type;

  public BoolArray(Pointer<?,? extends Type<?>> pointer, BoolArrayType type) {
    this.pointer = pointer;
    this.type = type;
  }

  @Override
  public BooleanIterator iterator() {
    return new BoolArrayIterator();
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
