package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.BinaryList;
import org.jspecify.annotations.Nullable;

import java.util.List;

public sealed abstract class PrimitiveListType<SELF extends PrimitiveListType<SELF, T, L>, T, L extends BinaryList<L, SELF, T>> extends BasicListType<SELF, T, L> permits BoolListType, Float32ListType, Float64ListType, Int16ListType, Int32ListType, Int64ListType, Int8ListType, Uint16ListType, Uint32ListType, Uint8ListType {

  protected PrimitiveListType(int position, @Nullable ComplexType<?> parent, BasicTypeInfo basicTypeInfo, @Nullable L constantValue, Expression lengthExpression) {
    super(position, basicTypeInfo, parent, constantValue, lengthExpression);
  }

  @Override
  public T get(Pointer<?, ? extends Type<?>> pointer, long index) {
    throw new UnsupportedOperationException("get(Pointer, long) not supported for " + getClass().getSimpleName() + ". Use get" + basicTypeInfo.title() + "(Pointer, long) instead.");
  }

  @Override
  public List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    throw new UnsupportedOperationException("getList(Pointer, long, long) not supported for " + getClass().getSimpleName() + ". Use get" + basicTypeInfo.title() + "List(Pointer, long, long) instead.");
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("set(Pointer, long, T) not supported for " + getClass().getSimpleName() + ". Use set" + basicTypeInfo.title() + "(Pointer, long, " + basicTypeInfo.primitive() + ") instead.");
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    throw new UnsupportedOperationException("set(Pointer, long, T[]) not supported for " + getClass().getSimpleName() + ". Use set" + basicTypeInfo.title() + "(Pointer, long, " + basicTypeInfo.primitive() + "[]) instead.");
  }

  @Override
  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    throw new UnsupportedOperationException("add(Pointer, long, T) not supported for " + getClass().getSimpleName() + ". Use add" + basicTypeInfo.title() + "(Pointer, long, " + basicTypeInfo.primitive() + ") instead.");
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    throw new UnsupportedOperationException("add(Pointer, long, T[]) not supported for " + getClass().getSimpleName() + ". Use add" + basicTypeInfo.title() + "(Pointer, long, " + basicTypeInfo.primitive() + "[]) instead.");
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    throw new UnsupportedOperationException("add(Pointer, T) not supported for " + getClass().getSimpleName() + ". Use add" + basicTypeInfo.title() + "(Pointer, " + basicTypeInfo.primitive() + ") instead.");
  }
}
