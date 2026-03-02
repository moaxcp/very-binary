package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.LengthChangeReason.*;

sealed interface ValueType<SELF extends ValueType<SELF, T>, T> permits IndexedValueType, PrimitiveType, StructType {
  int getPosition();

  @Nullable T getConstantValue();

  List<ByteLengthListener> getByteLengthListeners();

  List<ValueChangeListener> getValueChangeListeners();

  default SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    getValueChangeListeners().addAll(valueChangeListeners);
    return (SELF) this;
  }

  default SELF addValueChangeListener(ValueChangeListener listener) {
    getValueChangeListeners().add(listener);
    return (SELF) this;
  }

  default void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, T value) {
    if (!Objects.equals(getConstantValue(), value)) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value: " + value + " constant: " + getConstantValue());
    }
  }

  long getOffset(Pointer<?, ? extends Type<?>> pointer);

  long getByteLength(Pointer<?, ? extends Type<?>> pointer);

  /**
   * Should always call {@link #isConstant()} since all ValueTypes know their constant value
   * @param parent
   * @return
   */
  default boolean isConstant(@Nullable Type<?> parent) {
    return isConstant();
  }

  default boolean isConstant() {
    return getConstantValue() != null;
  }

  boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer);

  T get(Pointer<?, ? extends Type<?>> pointer);

  void set(Pointer<?, ? extends Type<?>> pointer, T value);

  default void remove(Pointer<?, ? extends Type<?>> pointer) {
    if (isFixedLength(pointer)) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length " + getClass().getSimpleName() + " at position " + getPosition());
    }
    callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer)));
  }

  void callWithByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Runnable runnable);

  default void notifyValueChange(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Object oldValue, Object newValue) {
    for(ValueChangeListener listener : getValueChangeListeners()) {
      listener.valueChanged(reason, pointer, oldValue, newValue);
    }
  }
}
