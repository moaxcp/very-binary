package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.PrimitiveListType;
import com.github.moaxcp.verybinary.Type;

public abstract class PrimitiveList<SELF extends PrimitiveList<SELF, T, E>, T  extends PrimitiveListType<T, E, SELF>, E> extends BinaryList<SELF, T, E> {

  protected PrimitiveList(Pointer<?, ? extends Type<?>> pointer, T type, long indexOffset, long length, boolean checkLength) {
    super(pointer, type, indexOffset, length, checkLength);
  }

  @Override
  public final E get(long index) {
    throw new UnsupportedOperationException("get is not supported for primitive lists");
  }

  public final void set(long index, E value) {
    throw new UnsupportedOperationException("set is not supported for primitive lists");
  }
}
