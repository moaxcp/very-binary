package com.github.moaxcp.verybinary;

import java.util.List;
import java.util.Objects;

sealed interface ListValueType<SELF extends ValueType<SELF, List<T>>, T> extends IndexedValueType<SELF, List<T>> permits StructListType {

  default long getConstantValueSize(Pointer<?, ? extends Type<?>> pointer) {
    return getConstantValue().size();
  }

  default void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(getConstantValue().get(Math.toIntExact(index)), value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + getConstantValue());
    }
  }

  default void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    if (isConstantValue(pointer.getType())) {
      if (getConstantValue().size() != values.size()) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values + " constant: " + getConstantValue());
      }
      for (var i = 0; i < values.size(); i++) {
        if (!Objects.equals(getConstantValue().get(i), values.get(i))) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values.get(i) + " constant: " + getConstantValue().get(i));
        }
      }
    }
  }

  T get(Pointer<?, ? extends Type<?>> pointer, long index);

  List<T> get(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  void set(Pointer<?, ? extends Type<?>> pointer, long index, T value);

  default void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    set(pointer, 0, values);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values);

  default void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    add(pointer, getLength(pointer), value);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    add(pointer, getLength(pointer), values);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (isFixedLength(pointer)) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant length: " + getLength(pointer) + " index: " + index);
    }
    checkForConstantValue(pointer, index, value);
    allocate(pointer, index);
    set(pointer, index, value);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    allocate(pointer, index, values.size());
    set(pointer, index, values);
  }
}
