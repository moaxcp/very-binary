package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructListType extends Type<StructListType> implements ListValueType<StructListType, Struct> {
  @Nullable
  private final List<Struct> constantValue;
  @Nullable
  private final Expression lengthExpression;
  @Nullable
  private final Expression byteLengthExpression;

  private final List<LengthListener> lengthListeners = new ArrayList<>();
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final StructType structType;

  public StructListType(int position, @Nullable List<Struct> constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression, StructType structType) {
    super(position);
    if (structType.getPosition() != 0) {
      throw new IllegalArgumentException("Struct type must have position 0");
    }
    structType.addByteLengthChangeListener((reason, pointer, previous, current) -> {
      if (getByteLengthListeners().isEmpty()) {
        return;
      }
      if (pointer.getParentOffset() == -1) {
        throw new IllegalStateException("Cannot call byte length change listener on root pointer");
      }
      var change = current - previous;
      var parent = new Struct(pointer.getParentOffset(), structType, pointer.getByteArray());
      parent.removeByteArrayListener();
      var length = parent.getByteLength();
      for (var listener : getByteLengthListeners()) {
        listener.byteLengthChanged(reason, parent, length - change, length);
      }
    });
    this.constantValue = constantValue;
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    this.structType = structType;
  }

  @Override
  public StructListType copy(int position) {
    return new StructListType(position, constantValue, lengthExpression, byteLengthExpression, structType);
  }

  @Override
  public long getConstantValueSize() {
    return constantValue.size();
  }

  @Override
  public @Nullable List<Struct> getConstantValue() {
    return constantValue;
  }

  @Override
  public @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
  }

  @Override
  public List<ValueChangeListener> getValueChangeListeners() {
    return valueChangeListeners;
  }

  @Override
  public @Nullable Expression getLengthExpression() {
    return lengthExpression;
  }

  public List<LengthListener> getLengthListeners() {
    return lengthListeners;
  }

  @Override
  public long getElementAllocationLength() {
    return structType.getAllocationLength(null);
  }

  @Override
  public long getAllocationLength(@Nullable Type<?> parent) {
    return ListValueType.super.getAllocationLength(parent);
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    long byteLength = 0;
    long arrayLength = getLength(pointer);
    for (long i = 0; i < arrayLength; i++) {
      byteLength += getByteLength(pointer, i);
    }
    return byteLength;
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    long byteLength = 0;
    var struct = new Struct(getOffset(pointer, index), structType, pointer.getByteArray());
    struct.removeByteArrayListener();
    for (int j = 0; j < structType.getFields().size(); j++) {
      var field = structType.getFields().get(j);
      byteLength += field.getByteLength(struct);
    }
    return byteLength;
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    long result = 0;
    for (long i = 0; i < length; i++) {
      result += getByteLength(pointer, index + i);
    }
    return result;
  }

  @Override
  public boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
    var length = getLength(pointer);
    for (long i = 0; i < length; i++) {
      var struct = new Struct(getOffset(pointer, i), structType, pointer.getByteArray());
      struct.removeByteArrayListener();
      for (int j = 0; j < structType.getFields().size(); j++) {
        if(!structType.getFields().get(j).isFixedLength(struct)) {
          return false;
        }
      }
    }
    return lengthExpression == null || lengthExpression.isConstant(pointer.getType());
  }

  @Override
  public List<Struct> get(Pointer<?, ? extends Type<?>> pointer) {
    var length = getLength(pointer);
    var result = new ArrayList<Struct>();
    for (long i = 0; i < length; i++) {
      result.add(get(pointer, i));
    }
    return result;
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer, long index) {
    var offset = getOffset(pointer, index);
    return new Struct(offset, structType, pointer.getByteArray());
  }

  @Override
  public List<Struct> get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    var result = new ArrayList<Struct>();
    for (long i = 0; i < length; i++) {
      result.add(get(pointer, index + i));
    }
    return result;
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, Struct value) {
    checkForConstantValue(pointer, index, value);
    if (!valueChangeListeners.isEmpty()) {
      var old = new Struct(pointer.getParentOffset(), getOffset(pointer, index), structType, pointer.getByteArray());
      if (!old.equals(value)) {
        pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index), value.getByteArray(), value.getOffset(), value.getByteLength());
        notifyValueChange(SET_VALUE, pointer, old, value);
      }
      old.removeByteArrayListener();
    } else {
      pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index), value.getByteArray(), value.getOffset(), value.getByteLength());
    }
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, List<Struct> values) {
      for (int i = 0; i < values.size(); i++) {
        set(pointer, index + i, values.get(i));
      }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    var length = getLength(pointer);
    for (long $ = 0; $ < length; $++) {
      var struct = new Struct(true, pointer.getOffset(), getOffset(pointer, $), structType, pointer.getByteArray());
      struct.removeByteArrayListener();
      if (structType.isConstant(null)) {
        struct.getByteArray().addInt8(getOffset(pointer), structType.getConstantArray().getAllocatedBytes());
      } else {
        for (int i = 0; i < structType.getFields().size(); i++) {
          structType.getType(i).allocate(pointer);
        }
      }
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        if (isConstantValue(pointer.getType())) {
          pointer.getByteArray().addInt8(getOffset(pointer), structType.getConstantArray().getAllocatedBytes());
        } else {
          var struct = new Struct(true, pointer.getOffset(), getOffset(pointer, index), structType, pointer.getByteArray());
          struct.removeByteArrayListener();
          for (int i = 0; i < structType.getFields().size(); i++) {
            structType.getType(i).allocate(struct);
          }
        }
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        for (long i = 0; i < length; i++) {
          if (isConstantValue(pointer.getType())) {
            pointer.getByteArray().addInt8(getOffset(pointer), structType.getConstantArray().getAllocatedBytes());
          } else {
            var struct = new Struct(true, pointer.getOffset(), getOffset(pointer, index + i), structType, pointer.getByteArray());
            struct.removeByteArrayListener();
            for (int j = 0; j < structType.getFields().size(); j++) {
              structType.getType(j).allocate(struct);
            }
          }
        }
      });
    });
  }
}
