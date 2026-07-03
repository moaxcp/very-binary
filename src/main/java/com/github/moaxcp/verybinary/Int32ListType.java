package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.Int32List;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.github.moaxcp.verybinary.BasicTypeInfo.INT32;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Int32ListType extends PrimitiveListType<Int32ListType, Integer, Int32List> {

  public Int32ListType(int position, @Nullable ComplexType parent, @Nullable Int32List constantValue, Expression lengthExpression) {
    super(position, parent, INT32, constantValue, lengthExpression);
  }

  @Override
  public Int32ListType copy(int position) {
    return new Int32ListType(position, getParent(), constantValue, lengthExpression);
  }

  @Override
  public Int32List get(Pointer<?, ? extends Type<?>> pointer) {
    return new Int32List(pointer, this);
  }

  @Override
  public Int32List get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return new Int32List(pointer, this, index, length);
  }

  public int getInt32(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getInt32(getOffset(pointer, index));
  }

  public int[] getInt32Array(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt32(getOffset(pointer), length);
  }

  public int[] getInt32Array(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt32(getOffset(pointer, index), length);
  }

  public List<Integer> getList(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return pointer.getByteArray().getInt32List(getOffset(pointer), length);
  }

  public List<Integer> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return pointer.getByteArray().getInt32List(getOffset(pointer, index), length);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, Int32List value) {
    set(pointer, 0, value);
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, Int32List value) {
    checkIndex(pointer, index);
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, int[] values) {
    set(pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, int value) {
    checkForConstantValue();
    checkIndex(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, int[] values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Integer> values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, int value) {
    if (pointer.getByteArray().getInt32(getOffset(pointer, index)) == value) {
      return;
    }
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setInt32(getOffset(pointer, index), value);
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setInt32(getOffset(pointer, index), value);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Int32List values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, int[] values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setInt32(getOffset(pointer, index), values);
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setInt32(getOffset(pointer, index), values);
    }
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Integer> values) {
    if (!valueChangeListeners.isEmpty()) {
      var old = get(pointer).copy();
      pointer.getByteArray().setInt32(getOffset(pointer, index), values);
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      pointer.getByteArray().setInt32(getOffset(pointer, index), values);
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, int value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, int[] values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, List<Integer> values) {
    add(pointer, getLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, int value) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, int[] values) {
    if (isFixedLength()) {
      throw new IllegalStateException("Cannot add elements to fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, List<Integer> values) {
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
