package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.BinaryList;
import com.github.moaxcp.verybinary.math.ArithmeticExpression;
import org.jspecify.annotations.Nullable;

public sealed abstract class BasicListType<SELF extends ListType<SELF, T, L>, T, L extends BinaryList<L, SELF, T>> extends ListType<SELF, T, L> permits PrimitiveListType, Uint64ListType {
  protected final BasicTypeInfo basicTypeInfo;

  protected BasicListType(int position, @Nullable ComplexType<?> parent, BasicTypeInfo basicTypeInfo, @Nullable L constantValue, @Nullable ArithmeticExpression lengthExpression) {
    super(position, parent, constantValue, lengthExpression);
    this.basicTypeInfo = basicTypeInfo;
  }

  public BasicTypeInfo getBasicTypeInfo() {
    return basicTypeInfo;
  }

  @Override
  public long getElementAllocationByteLength() {
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    BasicListType<?, ?, ?> that = (BasicListType<?, ?, ?>) o;
    return basicTypeInfo == that.basicTypeInfo;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + basicTypeInfo.hashCode();
    return result;
  }
}
