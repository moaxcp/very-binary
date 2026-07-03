package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.StructList;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.ByteArray.ba;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructListType extends Type<StructListType> implements ListValueType<StructListType, Struct> {
  @Nullable
  private final StructList constantValue;
  @Nullable
  private final Expression lengthExpression;
  @Nullable
  private final Expression byteLengthExpression;

  private final List<LengthListener> lengthListeners = new ArrayList<>();
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final StructType structType;

  /**
   * constantValue is ByteArray containing the list of structs. StructType does not need to be constant because the array
   * will always be used.
   * @param position
   * @param constantValue
   * @param lengthExpression
   * @param byteLengthExpression
   * @param structType
   */
  public StructListType(int position, @Nullable ByteArray constantValue, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression, StructType structType) {
    super(position);
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
    if (constantValue != null) {
      var pointer = new Struct(structType().type(this).build(), constantValue);
      this.constantValue = new StructList(pointer, this, 0, getLength(pointer));
    } else {
      this.constantValue = null;
    }
    this.lengthExpression = lengthExpression;
    this.byteLengthExpression = byteLengthExpression;
    this.structType = structType;
    if (structType.isConstant()) {
      throw new IllegalArgumentException("structType cannot be constant");
    }
    if (lengthExpression == null && byteLengthExpression == null && !isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot both be null unless there is a constantValue");
    } else if ((lengthExpression != null || byteLengthExpression != null) && isConstant()) {
      throw new IllegalArgumentException("lengthExpression and byteLengthExpression cannot be set when value is constant.");
    }
  }

  @Override
  public StructListType copy(int position) {
    return new StructListType(position, constantValue.getPointer().getByteArray(), lengthExpression, byteLengthExpression, structType);
  }

  @Override
  public boolean isConstant(@Nullable ComplexType parent) {
    return ListValueType.super.isConstant(parent);
  }

  @Override
  public long getConstantValueLength() {
    return constantValue != null ? constantValue.size() : 0;
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
  public long getAllocationLength(@Nullable ComplexType parent) {
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
    return structType.isFixedLength(pointer) && (lengthExpression == null || lengthExpression.isConstant(pointer.getType()));
  }

  @Override
  public StructList get(Pointer<?, ? extends Type<?>> pointer) {
    return new StructList(pointer, this, 0, getLength(pointer));
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer, long index) {
    var offset = getOffset(pointer, index);
    return new Struct(offset, structType, pointer.getByteArray());
  }

  @Override
  public List<Struct> get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return new StructList(pointer, this, index, length);
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
    if (this.isConstant()) {
      pointer.getByteArray().addInt8(getOffset(pointer), getConstantValueBytes());
    } else {
      var length = getLength(pointer);
      for (long $ = 0; $ < length; $++) {
        var struct = new Struct(true, pointer.getOffset(), getOffset(pointer, $), structType, pointer.getByteArray());
        struct.removeByteArrayListener();
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
        if (this.isConstant()) {
          pointer.getByteArray().addInt8(getOffset(pointer), getConstantValueBytes());
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

  private byte[] getConstantValueBytes() {
    long length = 0;
    for (var s : constantValue) {
      length += s.getByteLength();
    }
    var bytes = ba(length);
    for (var s : constantValue) {
      bytes.int8(s.getByteArray().getAllocatedBytes());
    }
    return bytes.getBytes();
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        for (long i = 0; i < length; i++) {
          if (this.isConstant()) {
            pointer.getByteArray().addInt8(getOffset(pointer), getConstantValueBytes());
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
