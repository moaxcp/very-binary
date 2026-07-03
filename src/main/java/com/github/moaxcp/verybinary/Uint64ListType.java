package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.Uint64List;
import org.jspecify.annotations.Nullable;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Uint64ListType extends PrimitiveListType<Uint64ListType, BigInteger, Uint64List> {

  public Uint64ListType(int position, @Nullable ComplexType parent, @Nullable Uint64List constantValue, Expression lengthExpression) {
    super(position, parent, UINT64, constantValue, lengthExpression);
  }

  @Override
  public Uint64ListType copy(int position) {
    return new Uint64ListType(position, getParent(), constantValue, lengthExpression);
  }

  @Override
  public Uint64List get(Pointer<?, ? extends Type<?>> pointer) {
    return new Uint64List(pointer, this);
  }

  @Override
  public Uint64List get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return new Uint64List(pointer, this, index, length);
  }

  public BigInteger getUint64(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getUint64(getOffset(pointer, index));
  }

  public BigInteger[] getUint64Array(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getUint64(getOffset(pointer), length);
  }

  public BigInteger[] getUint64Array(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getUint64(getOffset(pointer, index), length);
  }

  public List<BigInteger> getList(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
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

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, Uint64List value) {
    checkIndex(pointer, index);
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, BigInteger[] values) {
    set(pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    checkForConstantValue();
    checkIndex(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, BigInteger[] values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger value) {
    if (pointer.getByteArray().getUint64(getOffset(pointer, index)).equals(value)) {
      return;
    }
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Uint64List values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, BigInteger[] values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setUint64(getOffset(pointer, index), values);
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

  public void add(Pointer<?, ? extends Type<?>> pointer, List<BigInteger> values) {
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

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<BigInteger> values) {
    if (isFixedLength()) {
      throw new IllegalStateException("Cannot add elements to fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (this.isConstant()) {
      constantValue.replaceBytes(pointer.getByteArray(), getOffset(pointer), 0);
    } else {
      long length = getByteLength(pointer);
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), length);
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(reason, pointer, index, 1);
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (this.isConstant()) {
      throw new IllegalStateException("Cannot allocate element in constantValue " + constantValue);
    }
    callWithLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), length * basicTypeInfo.size());
      });
    });
  }
}
