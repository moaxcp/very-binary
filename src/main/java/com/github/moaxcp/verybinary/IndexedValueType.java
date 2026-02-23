package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.github.moaxcp.verybinary.LengthChangeReason.*;

public sealed interface IndexedValueType<SELF extends ValueType<SELF, IT>,IT> extends ValueType<SELF, IT> permits ArrayValueType, ListValueType {

  @Nullable Expression getLengthExpression();

  @Nullable Expression getByteLengthExpression();

  List<LengthListener> getLengthListeners();

  default SELF addLengthChangeListeners(List<LengthListener> lengthChange) {
    getLengthListeners().addAll(lengthChange);
    return (SELF) this;
  }

  default SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    getValueChangeListeners().addAll(valueChangeListeners);
    return (SELF) this;
  }

  default SELF addValueChangeListener(ValueChangeListener listener) {
    getValueChangeListeners().add(listener);
    return (SELF) this;
  }

  default long getOffset(Pointer<?, ? extends Type<?>> pointer, long index) {
    long offset = getOffset(pointer);
    for (int i = 0; i < index; i++) {
      offset += getByteLength(pointer, i);
    }
    return offset;
  }

  default long getLength(Pointer<?, ? extends Type<?>> pointer) {
    if (getLengthExpression() != null) {
      return getLengthExpression().evaluate(pointer);
    }
    if (getByteLengthExpression() != null) {
      return getByteLengthExpression().evaluate(pointer);
    } else if (isConstant()) {
      return getConstantValueSize();
    }
    throw new IllegalStateException("lengthExpression and byteLengthExpression are both null");
  }

  long getElementAllocationLength();

  long getConstantValueSize();

  default long getAllocationLength(@Nullable Type<?> parent) {
    if (this.isConstant()) {
      return getElementAllocationLength();
    }
    if (getLengthExpression() != null) {
      if (getLengthExpression().isConstant(parent)) {
        return getLengthExpression().constantValue(parent) * getElementAllocationLength();
      } else {
        return getLengthExpression().defaultValue(parent) * getElementAllocationLength();
      }
    }
    if (getByteLengthExpression() != null) {
      if (getByteLengthExpression().isConstant(parent)) {
        return getByteLengthExpression().constantValue(parent) / getElementAllocationLength();
      } else if (getByteLengthExpression().defaultValue(parent) != 0) {
        return getByteLengthExpression().defaultValue(parent) / getElementAllocationLength();
      } else {
        return 0;
      }
    }
    return 0;
  }

  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index);

  long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length);

  @Override
  default boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    if (isConstant()) {
      return true;
    }
    var expression = getLengthExpression() != null ? getLengthExpression() : getByteLengthExpression();
    return expression.isConstant(pointer.getType());
  }

  default void checkIndex(Pointer<?, ? extends Type<?>> pointer, long index) {
    var length = getLength(pointer);
    if (index >= length || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " length: " + length);
    }
  }

  default void checkArrayRange(Pointer<?, ? extends Type<?>> pointer, long start, long end) {
    var length = getLength(pointer);
    if (start < 0 || end > length || start > end) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " length: " + length + " start: " + start + " end: " + end);
    }
  }

  default void checkIndexAllocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    var newLength = getLength(pointer) + 1;
    if (index >= newLength || index < 0) {
      throw new ArrayIndexOutOfBoundsException(this.getClass().getSimpleName() + " at position " + getPosition() + " index: " + index + " new length: " + newLength);
    }
  }

  default void remove(Pointer<?, ? extends Type<?>> pointer) {
    callWithLengthChange(DEALLOCATED, pointer, -getLength(pointer),
        () -> callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer))));
  }

  default void remove(Pointer<?, ? extends Type<?>> pointer, long index) {
    remove(DEALLOCATED, pointer, index);
  }

  default void remove(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    remove(DEALLOCATED, pointer, index, length);
  }

  default void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    if (isFixedLength(pointer)) {
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

  default void remove(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    if (isFixedLength(pointer)) {
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

  default void allocate(Pointer<?, ? extends Type<?>> pointer, long index) {
    allocate(ALLOCATED, pointer, index);
  }

  default void allocate(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    allocate(ALLOCATED, pointer, index, length);
  }

  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index);

  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length);

  default void callWithLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long added, Runnable runnable) {
    if (getLengthListeners().isEmpty()) {
      runnable.run();
      return;
    }
    var oldLength = getLength(pointer);
    runnable.run();
    var newLength = oldLength + added;
    if (newLength != oldLength) {
      for (LengthListener listener : getLengthListeners()) {
        listener.arrayLengthChanged(reason, pointer, oldLength, newLength);
      }
    }
  }
}
