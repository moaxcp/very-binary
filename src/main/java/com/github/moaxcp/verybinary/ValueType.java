package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.LengthChangeReason.DEALLOCATED;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public sealed abstract class ValueType<SELF extends ValueType<SELF, T>, T> extends AbstractType<SELF> permits BasicType, ListType, StructType {

  protected final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  @Nullable
  protected T constantValue;

  protected ValueType(int position, @Nullable T constantValue, @Nullable ComplexType<?> parent) {
    super(position, parent);
    this.constantValue = constantValue;
  }

  @Nullable
  public T getConstantValue() {
    return constantValue;
  }

  /**
   * Checks if this type is constant and throws an exception if it is. This method should be called in all methods that
   * modify the value of this type.
   */
  protected final void checkForConstantValue() {
    if (isConstant()) {
      throw new IllegalStateException(getClass().getSimpleName() + " at position " + getPosition() + " is constant value");
    }
  }

  /**
   * Checks if the constant value has been set in the pointer. If the value is not set an exception is thrown..
   * @param pointer
   */
  protected void checkConstantValueSet(Pointer<?, ? extends Type<?>> pointer) {
    var value = get(pointer);
    if (!constantValue.equals(value)) {
      throw new IllegalStateException("Constant value " + constantValue + " does not match value " + value);
    }
  }

  /**
   * Should always call {@link #isConstant()} since all ValueTypes know their constant value
   * @return
   */
  public boolean isConstant() {
    return getConstantValue() != null;
  }

  protected final SELF addValueChangeListener(ValueChangeListener listener) {
    valueChangeListeners.add(listener);
    return (SELF) this;
  }

  protected final SELF addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    this.valueChangeListeners.addAll(valueChangeListeners);
    return (SELF) this;
  }

  public abstract T get(Pointer<?, ? extends Type<?>> pointer);

  public final void set(Pointer<?, ? extends Type<?>> pointer, T value) {
    checkForConstantValue();
    setUnchecked(SET_VALUE, pointer, value);
  }

  protected abstract void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, T value);

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (this.isConstant()) {
      setUnchecked(SET_VALUE, pointer, constantValue);
    } else {
      long length = getByteLength(pointer);
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), length);
    }
  }

  public void remove(Pointer<?, ? extends Type<?>> pointer) {
    if (isFixedLength()) {
      throw new UnsupportedOperationException("Cannot remove element from fixed length " + getClass().getSimpleName() + " at position " + getPosition());
    }
    callWithByteLengthChange(DEALLOCATED, pointer, () -> pointer.getByteArray().removeInt8(getOffset(pointer), getByteLength(pointer)));
  }

  public final void notifyValueChange(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, T oldValue, T newValue) {
    for(ValueChangeListener listener : valueChangeListeners) {
      listener.valueChanged(reason, pointer, oldValue, newValue);
    }
  }
}
