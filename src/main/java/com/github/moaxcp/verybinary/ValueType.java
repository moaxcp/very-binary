package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.LengthChangeReason.*;

sealed interface ValueType<SELF extends ValueType<SELF, T>, T> permits PrimitiveType, StructType {
  int getPosition();

  @Nullable T getConstantValue();

  @Nullable Expression getLengthExpression();

  @Nullable Expression getByteLengthExpression();

  List<ArrayLengthListener> getArrayLengthListeners();

  List<ByteLengthListener> getByteLengthListeners();

  List<ValueChangeListener> getValueChangeListeners();

  SELF addArrayLengthChangeListeners(List<ArrayLengthListener> arrayLengthChange);

  SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners);

  SELF addValueChangeListener(ValueChangeListener listener);

  long getOffset(Pointer<?, ? extends Type<?>> pointer);

  default long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    for (int i = 0; i < index; i++) {
      offset += getByteLength(pointer, i);
    }
    return offset;
  }

  default boolean isArray() {
    return getLengthExpression() != null || getByteLengthExpression() != null;
  }

  long getByteLength(Pointer<?, ? extends Type<?>> pointer);

  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index);

  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  default long getArrayLength(Pointer<?, ? extends Type<?>> pointer) {
    if (!isArray()) {
      return 1;
    }
    return getLengthExpression().evaluate(pointer);
  }

  default boolean isConstant(Type<?> type) {
    return getConstantValue() != null && (getLengthExpression() == null || getLengthExpression().isConstant(type));
  }

  default boolean isConstantValue(Type<?> type) {
    return getConstantValue() != null;
  }

  boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer);

  default T get(Pointer<?, ? extends Type<?>> pointer) {
    return get(pointer, 0);
  }

  T get(Pointer<?, ? extends Type<?>> pointer, long index);

  T[] getArray(Pointer<?, ? extends Type<?>> pointer);

  T[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  List<T> getList(Pointer<?, ? extends Type<?>> pointer);

  List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  default void checkIndex(Pointer<?, ? extends Type<?>> pointer, long index) {
    var length = getArrayLength(pointer);
    if (index >= length || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " length: " + length);
    }
  }

  default void checkArrayRange(Pointer<?, ? extends Type<?>> pointer, long start, long end) {
    var length = getArrayLength(pointer);
    if (start < 0 || end > length || start > end) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " length: " + length + " start: " + start + " end: " + end);
    }
  }

  default void set(Pointer<?, ? extends Type<?>> pointer, T value) {
    set(pointer, 0, value);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, T value);

  default void set(Pointer<?, ? extends Type<?>> pointer, T... values) {
    set(pointer, 0, values);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, T... values);

  default void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    set(pointer, 0, values);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values);

  default void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(getConstantValue(), value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + getConstantValue());
    }
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    add(pointer, getArrayLength(pointer), value);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, T... values) {
    add(pointer, getArrayLength(pointer), values);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    add(pointer, getArrayLength(pointer), values);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getArrayLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    set(pointer, index, value);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, T... values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, index, values.length);
    set(pointer, index, values);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer,long index, List<T> values) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition());
    }
    allocate(pointer, index, values.size());
    set(pointer, index, values);
  }

  default void allocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(ALLOCATED, pointer, index);
  }

  default void allocate(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    allocate(ALLOCATED, pointer, index, length);
  }

  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index);

  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length);

  default void checkIndexAllocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    var newLength = getArrayLength(pointer) + 1;
    if (index >= newLength || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " new length: " + newLength);
    }
  }

  default void remove(Pointer<?, ? extends Type<?>> pointer) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot remove from non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove fixed length array " + getClass().getSimpleName() + " at position " + getPosition());
    }
    callWithArrayLengthChange(DEALLOCATED, pointer, -getArrayLength(pointer),
        () -> callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer))));
  }

  void callWithByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Runnable runnable);

  default void remove(Pointer<?, ? extends Type<?>> pointer, long index) {
    remove(DEALLOCATED, pointer, index);
  }

  default void remove(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    remove(DEALLOCATED, pointer, index, length);
  }

  default void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
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

  default void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot remove from non-array type at position " + getPosition());
    }
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length array " + getClass().getSimpleName() + " at position " + getPosition() + " index: " + index);
    }
    if (reason != RESIZED_BY_LENGTH_FIELD) {
      checkArrayRange(pointer, index, index + length - 1);
    }
    callWithArrayLengthChange(reason, pointer, -length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        pointer.getByteArray().removeInt8(getOffset(pointer, index), getByteLength(pointer, index, length));
      });
    });
  }

  default void callWithArrayLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long added, Runnable runnable) {
    if (getArrayLengthListeners().isEmpty()) {
      runnable.run();
      return;
    }
    var oldLength = getArrayLength(pointer);
    runnable.run();
    var newLength = oldLength + added;
    if (newLength != oldLength) {
      for (ArrayLengthListener listener : getArrayLengthListeners()) {
        listener.arrayLengthChanged(reason, pointer, oldLength, newLength);
      }
    }
  }

  default void notifyValueChange(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, T oldValue, T newValue) {
    for(ValueChangeListener listener : getValueChangeListeners()) {
      listener.valueChanged(reason, pointer, index, oldValue, newValue);
    }
  }
}
