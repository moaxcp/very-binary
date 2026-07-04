package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.BinaryList;
import org.jspecify.annotations.Nullable;

public sealed abstract class BasicListType<SELF extends ListType<SELF, T, L>, T, L extends BinaryList<L, SELF, T>> extends ListType<SELF, T, L> permits PrimitiveListType, Uint64ListType {
  protected final BasicTypeInfo basicTypeInfo;

  protected BasicListType(int position, BasicTypeInfo basicTypeInfo, @Nullable ComplexType<?> parent, @Nullable L constantValue, @Nullable Expression lengthExpression) {
    super(position, parent, constantValue, lengthExpression);
    this.basicTypeInfo = basicTypeInfo;
  }

  @Override
  public long getElementAllocationLength() {
    return basicTypeInfo.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    return getLength(pointer) * basicTypeInfo.size();
  }

  @Override
  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    return basicTypeInfo.size();
  }

  @Override
  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return basicTypeInfo.size() * length;
  }

  @Override
  public boolean isElementFixedLength() {
    return true;
  }
}
