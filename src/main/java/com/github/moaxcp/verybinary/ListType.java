package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.BinaryList;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.LengthChangeReason.*;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public sealed abstract class ListType<SELF extends ListType<SELF, T, L>, T, L extends BinaryList<L, T>> extends ValueType<SELF, L> permits BasicListType {

  protected final Expression lengthExpression;
  protected final List<LengthListener> lengthListeners = new ArrayList<>();

  protected ListType(int position, @Nullable ComplexType parent, @Nullable L constantValue, @Nullable Expression lengthExpression) {
    super(position, constantValue, parent);
    if (lengthExpression == null && constantValue == null) {
      throw new IllegalArgumentException("lengthExpression and constantValue cannot both be null");
    }
    if (lengthExpression != null && constantValue != null) {
      throw new IllegalArgumentException("lengthExpression and constantValue cannot both be set");
    }
    if (constantValue != null) {
      this.constantValue = constantValue;
      this.lengthExpression = new Expression() {
        @Override
        public boolean isConstant(ComplexType parent) {
          return true;
        }

        @Override
        public long constantValue(ComplexType parent) {
          return constantValue.size64();
        }

        @Override
        public long defaultValue(ComplexType parent) {
          return 0;
        }

        @Override
        public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
          return constantValue.size64();
        }
      };
    } else {
      this.lengthExpression = lengthExpression;
    }
  }

  protected final SELF addLengthListener(LengthListener lengthChange) {
    lengthListeners.add(lengthChange);
    return (SELF) this;
  }
  protected final SELF addLengthListeners(List<LengthListener> lengthChange) {
    lengthListeners.addAll(lengthChange);
    return (SELF) this;
  }

  public long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    for (int i = 0; i < index; i++) {
      offset += getByteLength(pointer, i);
    }
    return offset;
  }

  public long getLength(Pointer<?, ? extends Type<?>> pointer) {
    return lengthExpression.evaluate(pointer);
  }

  protected long getAllocationLength() {
    if (lengthExpression.isConstant(parent)) {
      return lengthExpression.constantValue(parent) * getElementAllocationLength();
    } else {
      return lengthExpression.defaultValue(parent) * getElementAllocationLength();
    }
  }

  protected abstract long getElementAllocationLength();

  abstract long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index);

  abstract long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  @Override
  public boolean isFixedLength() {
    return lengthExpression.isConstant(parent) && isElementFixedLength();
  }

  public abstract boolean isElementFixedLength();

  protected final void checkIndex(Pointer<?, ? extends Type<?>> pointer, long index) {
    var length = getLength(pointer);
    if (index >= length || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " length: " + length);
    }
  }

  protected final void checkArrayRange(Pointer<?, ? extends Type<?>> pointer, long start, long end) {
    var length = getLength(pointer);
    if (start < 0 || end >= length || start > end) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " length: " + length + " start: " + start + " end: " + end);
    }
  }

  protected final void checkIndexAllocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    var newLength = getLength(pointer) + 1;
    if (index >= newLength || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " new length: " + newLength);
    }
  }

  @Override
  public L get(Pointer<?, ? extends Type<?>> pointer) {
    return get(pointer, 0, getLength(pointer));
  }

  public abstract T get(Pointer<?, ? extends Type<?>> pointer, long index);

  public abstract L get(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  public T[] getArray(Pointer<?, ? extends Type<?>> pointer) {
    long length = getLength(pointer);
    checkIndex(pointer, length - 1);
    return getArray(pointer, 0, length);
  }

  public abstract T[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  public abstract List<T> getList(Pointer<?, ? extends Type<?>> pointer);

  public abstract List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  protected abstract void setUnchecked(ValueChangeReason reason, Pointer<?,? extends Type<?>> pointer, long index, T value);

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, L value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, T[] values) {
    set(pointer, 0, values);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public  void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    set(pointer, 0, values);
  }

  public final void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    checkForConstantValue();
    checkArrayRange(pointer, index, index + values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  protected final void setUnchecked(ValueChangeReason reason, Pointer<?,? extends Type<?>> pointer, L values) {
    setUnchecked(reason, pointer, 0, values);
  }

  protected final void setUnchecked(ValueChangeReason reason, Pointer<?,? extends Type<?>> pointer, long index, L values) {
    if (!valueChangeListeners.isEmpty()) {
      L old = get(pointer).copy();
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
      var newValue = get(pointer).copy();
      notifyValueChange(reason, pointer, old, newValue);
    } else {
      values.replaceBytes(pointer.getByteArray(), getOffset(pointer, index), getByteLength(pointer, index, getLength(pointer) - index));
    }
  }

  protected abstract void setUnchecked(ValueChangeReason reason, Pointer<?,? extends Type<?>> pointer, long index, T[] values);

  protected abstract void setUnchecked(ValueChangeReason reason, Pointer<?,? extends Type<?>> pointer, long index, List<T> values);

  public void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, T[] values) {
    add(pointer, getLength(pointer), values);
  }

  public final void add(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    add(pointer, getLength(pointer), values);
  }

  public final void add(Pointer<?, ? extends Type<?>> pointer, L value) {
    add(pointer, getLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public final void add(Pointer<?, ? extends Type<?>> pointer, long index, L value) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index, values.length);
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  public final void add(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    if (isFixedLength()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue();
    allocate(pointer, index, values.size());
    setUnchecked(SET_VALUE, pointer, index, values);
  }

  @Override
  public final void remove(Pointer<?, ? extends Type<?>> pointer) {
    if (isFixedLength()) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length " + getClass().getSimpleName() + " at position " + getPosition());
    }
    callWithLengthChange(DEALLOCATED, pointer, -getLength(pointer),
        () -> callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer))));
  }

  public final void remove(Pointer<?, ? extends Type<?>> pointer, long index) {
    remove(DEALLOCATED, pointer, index);
  }

  public final void remove(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    remove(DEALLOCATED, pointer, index, length);
  }

  protected final void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    if (isFixedLength()) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    if (reason != RESIZED_BY_LENGTH_FIELD) {
      checkIndex(pointer, index);
    }
    callWithLengthChange(reason, pointer, -1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        pointer.getByteArray().removeInt8(getOffset(pointer, index), getByteLength(pointer, index));
      });
    });
  }

  protected final void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (isFixedLength()) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    if (reason != RESIZED_BY_LENGTH_FIELD) {
      checkArrayRange(pointer, index, index + length - 1);
    }
    callWithLengthChange(reason, pointer, -length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        pointer.getByteArray().removeInt8(getOffset(pointer, index), getByteLength(pointer, index, length));
      });
    });
  }

  public final void allocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(ALLOCATED, pointer, index);
  }

  public final void allocate(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    allocate(ALLOCATED, pointer, index, length);
  }

  protected final void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(reason, pointer, index, 1);
  }

  protected final void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (this.isConstant()) {
      throw new IllegalStateException("Cannot allocate element when type has constantValue");
    }
    //todo check length is divisible by element size allocation length
    callWithLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().shiftBytesFor(getOffset(pointer, index), getElementAllocationLength() * length);
      });
    });
  }

  protected final void callWithLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long added, Runnable runnable) {
    if (lengthListeners.isEmpty()) {
      runnable.run();
      return;
    }
    var oldLength = getLength(pointer);
    runnable.run();
    var newLength = oldLength + added;
    if (newLength != oldLength) {
      for (LengthListener listener : lengthListeners) {
        listener.arrayLengthChanged(reason, pointer, oldLength, newLength);
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    ListType<?, ?, ?> listType = (ListType<?, ?, ?>) o;
    return lengthExpression.equals(listType.lengthExpression) && lengthListeners.equals(listType.lengthListeners);
  }

  @Override
  public int hashCode() {
    int result = lengthExpression.hashCode();
    result = 31 * result + lengthListeners.hashCode();
    return result;
  }
}
