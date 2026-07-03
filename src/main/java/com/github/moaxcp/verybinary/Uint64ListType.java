package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.Uint64List;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Uint64ListType extends BasicListType<Uint64ListType, BigInteger, Uint64List> {

  public Uint64ListType(int position, @Nullable ComplexType parent, @Nullable Uint64List constantValue, Expression lengthExpression) {
    super(position, UINT64, parent, constantValue, lengthExpression);
  }

  @Override
  public Uint64ListType copy(int position, @Nullable ComplexType parent) {
    return new Uint64ListType(position, parent, constantValue, lengthExpression);
  }

  @Override
  public Uint64List get(Pointer<?, ? extends Type<?>> pointer) {
    return new Uint64List(pointer, this);
  }

  @Override
  public BigInteger get(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getUint64(getOffset(pointer, index));
  }

  @Override
  public Uint64List get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return new Uint64List(pointer, this, index, length);
  }

  public BigInteger[] getArray(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    return pointer.getByteArray().getUint64(getOffset(pointer), length);
  }

  @Override
  public BigInteger[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint64(getOffset(pointer), length);
  }

  public List<BigInteger> getList(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    return pointer.getByteArray().getUint64List(getOffset(pointer), length);
  }

  public List<BigInteger> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint64List(getOffset(pointer, index), length);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, Uint64List value) {
    set(pointer, 0, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, BigInteger[] values) {
    set(pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger[] values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (pointer.getByteArray().getUint64(getOffset(pointer, index)).equals(value)) {
      return;
    }
    if (!valueChangeListeners.isEmpty()) {
      var old =get(pointer).copy();
      var newValue = old.copy();
      newValue.set(index, value);
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
    }
  }

  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger[] values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      var bytes = new ByteArray(getElementAllocationLength() * values.length).addUint64(0, values);
      pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index), bytes, 0, bytes.getAllocated());
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
    }
  }

  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      var bytes = new ByteArray(getElementAllocationLength() * values.size()).addUint64(0, values);
      pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index), bytes, 0, bytes.getAllocated());
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, BigInteger value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, BigInteger[] values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger[] values) {
    if (isFixedLength()) {
      throw new IllegalStateException("Cannot add elements to fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }
}
