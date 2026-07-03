package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.ListType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public abstract class PrimitiveList<SELF extends PrimitiveList<SELF, T>, T> extends BinaryList<SELF, T> {

  protected PrimitiveList(Pointer<?, ? extends Type<?>> pointer, ListType<?, T, SELF> type, long indexOffset, long length, boolean checkLength) {
    super(pointer, type, indexOffset, length, checkLength);
  }

  @Override
  public final T get(long index) {
    throw new UnsupportedOperationException("get is not supported for primitive lists");
  }

  public final void set(long index, T value) {
    throw new UnsupportedOperationException("set is not supported for primitive lists");
  }
}
