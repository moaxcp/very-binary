package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.LengthChangeReason.*;

public abstract sealed class ValueType<SELF extends ValueType<SELF, T>, T> extends Type<SELF> permits PrimitiveType, StructType {
  @Nullable
  protected final Expression lengthExpression;
  @Nullable
  protected final Expression byteLengthExpression;
  @Nullable
  protected final T constantValue;
  protected final List<ArrayLengthListener> arrayLengthListeners = new ArrayList<>();
  protected final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();

  public ValueType(int position) {
    super(position);
    this.lengthExpression = null;
    this.byteLengthExpression = null;
    this.constantValue = null;
  }

  public ValueType(int position, @Nullable T constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression) {
    super(position);
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    this.constantValue = constantValue;
  }

  public final @Nullable T getConstantValue() {
    return constantValue;
  }

  public final @Nullable Expression getLengthExpression() {
    return lengthExpression;
  }

  public final @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  public SELF addArrayLengthChangeListeners(List<ArrayLengthListener> arrayLengthChange) {
    arrayLengthListeners.addAll(arrayLengthChange);
    return (SELF) this;
  }

  public SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    this.valueChangeListeners.addAll(valueChangeListeners);
    return (SELF) this;
  }

  public SELF addValueChangeListener(ValueChangeListener listener) {
    this.valueChangeListeners.add(listener);
    return (SELF) this;
  }

  public long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    for (int i = 0; i < index; i++) {
      offset += getByteLength(pointer, i);
    }
    return offset;
  }

  public final boolean isArray() {
    return lengthExpression != null || byteLengthExpression != null;
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    if(!isArray()) {
      return getByteLength(pointer, 0);
    }
    var length = 0L;
    var l = getArrayLength(pointer);
    for (int i = 0; i < l; i++) {
      length += getByteLength(pointer, i);
    }
    return length;
  }

  public abstract long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index);

  public abstract long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  public long getArrayLength(Pointer<?, ? extends Type<?>> pointer) {
    if (!isArray()) {
      return 1;
    }
    return lengthExpression.evaluate(pointer);
  }

  public final boolean isConstant(Type<?> type) {
    return constantValue != null && (lengthExpression == null || lengthExpression.isConstant(type));
  }

  public final boolean isConstantValue(Type<?> type) {
    return constantValue != null;
  }

  public T get(Pointer<?, ? extends Type<?>> pointer) {
    return get(pointer, 0);
  }

  public abstract T get(Pointer<?, ? extends Type<?>> pointer, long index);

  public abstract T[] getArray(Pointer<?, ? extends Type<?>> pointer);

  public abstract T[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  public abstract List<T> getList(Pointer<?, ? extends Type<?>> pointer);

  public abstract List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  protected void checkIndex(Pointer<?, ? extends Type<?>> pointer, long index) {
    var length = getArrayLength(pointer);
    if (index >= length || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " length: " + length);
    }
  }

  protected void checkIndexRange(Pointer<?, ? extends Type<?>> pointer, long start, long end) {
    var length = getArrayLength(pointer);
    if (start < 0 || end >= length || start >= end) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " length: " + length + " start: " + start + " end: " + end);
    }
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, T value) {
    set(pointer, 0, value);
  }

  public abstract void set(Pointer<?, ? extends Type<?>> pointer, long index, T value);

  public void set(Pointer<?, ? extends Type<?>> pointer, T... values) {
    set(pointer, 0, values);
  }

  public abstract void set(Pointer<?, ? extends Type<?>> pointer, long index, T... values);

  public void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    set(pointer, 0, values);
  }

  public abstract void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values);

  //todo push down to each type to avoid wrapping
  protected void checkConstant(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (isConstant(pointer.getType()) && !Objects.equals(constantValue, value)) {
      throw new UnsupportedOperationException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, T... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, getArrayLength(pointer), values.length);
    set(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer,List<T> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, getArrayLength(pointer), values.size());
    set(pointer, getArrayLength(pointer), values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, index);
    set(pointer, index, value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, T... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, index, values.length);
    set(pointer, index, values);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer,long index, List<T> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, index, values.size());
    set(pointer, index, values);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(ALLOCATED, pointer, index);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    allocate(ALLOCATED, pointer, index, length);
  }

  abstract void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index);

  abstract void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length);

  void checkIndexAllocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    var newLength = getArrayLength(pointer) + 1;
    if (index >= newLength || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " new length: " + newLength);
    }
  }

  public final void remove(Pointer<?, ? extends Type<?>> pointer) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot remove from non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove fixed length array " + getClass().getSimpleName() + " at position " + getPosition());
    }
    callWithArrayLengthChange(DEALLOCATED, pointer, -getArrayLength(pointer),
        () -> callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer))));
  }

  public final void remove(Pointer<?, ? extends Type<?>> pointer, long index) {
    remove(DEALLOCATED, pointer, index);
  }

  public final void remove(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    remove(DEALLOCATED, pointer, index, length);
  }

  final void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot remove from non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    if (reason != RESIZED_BY_LENGTH_FIELD) {
      checkIndex(pointer, index);
    }
    callWithArrayLengthChange(reason, pointer, -1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        pointer.getByteArray().removeInt8(getOffset(pointer, index), getByteLength(pointer, index));
      });
    });
  }

  final void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot remove from non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    if (reason != RESIZED_BY_LENGTH_FIELD) {
      checkIndexRange(pointer, index, index + length - 1);
    }
    callWithArrayLengthChange(reason, pointer, -length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        pointer.getByteArray().removeInt8(getOffset(pointer, index), getByteLength(pointer, index, length));
      });
    });
  }

  protected void callWithArrayLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long added, Runnable runnable) {
    if (arrayLengthListeners.isEmpty()) {
      runnable.run();
      return;
    }
    var oldLength = getArrayLength(pointer);
    runnable.run();
    var newLength = oldLength + added;
    if (newLength != oldLength) {
      for (ArrayLengthListener listener : arrayLengthListeners) {
        listener.arrayLengthChanged(reason, pointer, oldLength, newLength);
      }
    }
  }

  protected void notifyValueChange(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, T oldValue, T newValue) {
    for(ValueChangeListener listener : valueChangeListeners) {
      listener.valueChanged(reason, pointer, index, oldValue, newValue);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    ValueType<?, ?> valueType = (ValueType<?, ?>) o;
    return Objects.equals(lengthExpression, valueType.lengthExpression) && Objects.equals(arrayLengthListeners, valueType.arrayLengthListeners) && Objects.equals(constantValue, valueType.constantValue);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Objects.hashCode(lengthExpression);
    result = 31 * result + Objects.hashCode(arrayLengthListeners);
    result = 31 * result + Objects.hashCode(constantValue);
    return result;
  }
}
