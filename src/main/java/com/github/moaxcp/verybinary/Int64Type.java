package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ArrayLengthListener.ArrayLengthReason;
import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import static com.github.moaxcp.verybinary.Primitive.INT64;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_BY_ARRAY_LENGTH;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class Int64Type extends NumberType<Int64Type, Long> {

  public static Int64Type int64Type() {
    return int64Type(-1);
  }

  public static Int64Type int64Type(int position) {
    return new Int64Type(position);
  }

  public Int64Type(int position, @Nullable Long constantValue, @Nullable Expression lengthExpression) {
    super(position, INT64, constantValue, lengthExpression);
  }

  public Int64Type(int position) {
    super(position, INT64);
  }

  @Override
  protected Int64Type copy(int position) {
    return new Int64Type(position, constantValue, lengthExpression);
  }

  public long getInt64(Pointer<?, ? extends Type<?>> pointer) {
    return pointer.getByteArray().getInt64(getOffset(pointer));
  }

  public long getInt64(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    return pointer.getByteArray().getInt64(getOffset(pointer, index));
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_VALUE, pointer, 0, value);
  }

  void setForArrayLength(Pointer<?, ? extends Type<?>> pointer, long value) {
    setUnchecked(SET_BY_ARRAY_LENGTH, pointer, 0, (long) value);
  }

  public void set(Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    checkIndex(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  private void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    checkConstant(pointer, index, value);
    if (!valueChangeListeners.isEmpty()) {
      var old = pointer.getByteArray().getInt64(getOffset(pointer, index));
      pointer.getByteArray().setInt64(getOffset(pointer, index), value);
      notifyValueChange(reason, pointer, index, old, value);
    }
    pointer.getByteArray().setInt64(getOffset(pointer, index), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long value) {
    add(pointer, getArrayLength(pointer), value);
  }

  public void add(Pointer<?, ? extends Type<?>> pointer, long index, long value) {
    if (!isArray()) {
      throw new ArrayIndexOutOfBoundsException(getClass().getSimpleName() + " cannot add to non-array type at position " + getPosition() + " index: " + index + " length: " + 1);
    }
    allocate(pointer, index);
    setUnchecked(SET_VALUE, pointer, index, value);
  }

  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isArray()) {
      long length = getArrayLength(pointer);
      for (int i = 0; i < length; i++) {
        pointer.getByteArray().addInt64(getOffset(pointer, i), constantValue != null ? constantValue : 0L);
      }
    } else {
      pointer.getByteArray().addInt64(getOffset(pointer, 0), constantValue != null ? constantValue : 0L);
    }
  }

  @Override
  protected void allocate(ArrayLengthReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(pointer, () -> {
        checkIndexAllocate(pointer, index);
        pointer.getByteArray().addInt64(getOffset(pointer, index), constantValue != null ? constantValue : 0L);
      });
    });
  }
}
