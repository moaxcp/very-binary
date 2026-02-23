package com.github.moaxcp.verybinary;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

sealed interface ArrayValueType<SELF extends ValueType<SELF, T[]>, T> extends IndexedValueType<SELF, T[]> permits PrimitiveArrayType {

  default long getConstantValueSize(Pointer<?, ? extends Type<?>> pointer) {
    return getConstantValue().length;
  }

  default void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, T value) {
    if (this.isConstant() && !Objects.equals(getConstantValue()[Math.toIntExact(index)], value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + getConstantValue());
    }
  }

  default void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, T[] values) {
    if (this.isConstant()) {
      if (getConstantValue().length != values.length) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + Arrays.toString(values) + " constant: " + Arrays.toString(getConstantValue()));
      }
      for (var i = 0; i < values.length; i++) {
        if (!Objects.equals(getConstantValue()[i], values[i])) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values[i] + " constant: " + getConstantValue()[i]);
        }
      }
    }
  }

  default void checkForConstantValues(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    if (this.isConstant()) {
      if (getConstantValue().length != values.size()) {
        throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values + " constant: " + Arrays.toString(getConstantValue()));
      }
      for (var i = 0; i < values.size(); i++) {
        if (!Objects.equals(getConstantValue()[i], values.get(i))) {
          throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + values.get(i) + " constant: " + getConstantValue()[i]);
        }
      }
    }
  }

  default void checkIndexAllocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    var newLength = getLength(pointer) + 1;
    if (index >= newLength || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " new length: " + newLength);
    }
  }

  T get(Pointer<?, ? extends Type<?>> pointer, long index);

  T[] get(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  List<T> getList(Pointer<?, ? extends Type<?>> pointer);

  List<T> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  void set(Pointer<?, ? extends Type<?>> pointer, long index, T value);

  default void set(Pointer<?, ? extends Type<?>> pointer, T... values) {
    set(pointer, 0, values);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, T... values);

  default void set(Pointer<?, ? extends Type<?>> pointer, List<T> values) {
    set(pointer, 0, values);
  }

  void set(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values);

  default void add(Pointer<?, ? extends Type<?>> pointer, T value) {
    add(pointer, getLength(pointer), value);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, T... values) {
    add(pointer, getLength(pointer), values);
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

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, T... values) {
    allocate(pointer, index, values.length);
    set(pointer, index, values);
  }

  default void add(Pointer<?, ? extends Type<?>> pointer, long index, List<T> values) {
    allocate(pointer, index, values.size());
    set(pointer, index, values);
  }
}
