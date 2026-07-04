package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import com.github.moaxcp.verybinary.list.StructList;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructListType extends ListType<StructListType, Struct, StructList> {
  private final StructType structType;

  /**
   * constantValue is ByteArray containing the list of structs. StructType does not need to be constant because the array
   * will always be used.
   * @param position
   * @param constantValue
   * @param lengthExpression
   * @param structType
   */
  public StructListType(int position, @Nullable ComplexType<?> parent, @Nullable StructList constantValue, @Nullable Expression lengthExpression, StructType structType) {
    super(position, parent, constantValue, lengthExpression);
    this.structType = structType;
    structType.addByteLengthListener((reason, pointer, previous, current) -> {
      if (getByteLengthListeners().isEmpty()) {
        return;
      }
      if (pointer.getParentOffset() == -1) {
        throw new IllegalStateException("Cannot call byte length change listener on root pointer");
      }
      var change = current - previous;
      var element = new Struct(pointer.getParentOffset(), structType, pointer.getByteArray());
      element.removeByteArrayListener();
      var length = element.getByteLength();
      for (var listener : getByteLengthListeners()) {
        listener.byteLengthChanged(reason, element, length - change, length);
      }
    });
  }

  public StructType getStructType() {
    return structType;
  }

  @Override
  public StructListType copy(int position, @Nullable ComplexType<?> parent) {
    return new StructListType(position, parent, constantValue, lengthExpression, structType);
  }

  @Override
  public long getElementAllocationLength() {
    return structType.getAllocationLength();
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
    for (int j = 0; j < structType.getTypes().size(); j++) {
      var field = structType.getTypes().get(j);
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
  public boolean isElementFixedLength() {
    return false;
  }

  @Override
  public StructList get(Pointer<?, ? extends Type<?>> pointer) {
    return new StructList(pointer, this, 0, getLength(pointer));
  }

  @Override
  public StructList get(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    checkArrayRange(pointer, index, index + length);
    return new StructList(pointer, this, index, length);
  }

  @Override
  public List<Struct> getList(Pointer<?, ? extends Type<?>> pointer) {
    return List.of();
  }

  @Override
  public List<Struct> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    return List.of();
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer, long index) {
    checkIndex(pointer, index);
    var offset = getOffset(pointer, index);
    return new Struct(offset, structType, pointer.getByteArray());
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Struct value) {
    if (!valueChangeListeners.isEmpty()) {
      var old =get(pointer).copy();
      var newValue = old.copy();
      newValue.set(index, value);
      var oldStruct = new Struct(pointer.getParentOffset(), getOffset(pointer, index), structType, pointer.getByteArray());
      pointer.getByteArray().replace(oldStruct.getOffset(), oldStruct.getByteLength(), value.getByteArray(), value.getOffset(), value.getByteLength());
      notifyValueChange(SET_VALUE, pointer, old, newValue);
      oldStruct.removeByteArrayListener();
    } else {
      pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index), value.getByteArray(), value.getOffset(), value.getByteLength());
    }
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Struct[] values) {
    for(var i = 0; i < values.length; i++) {
      setUnchecked(reason, pointer, index + i, values[i]);
    }
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, List<Struct> values) {
    for(var i = 0; i < values.size(); i++) {
      setUnchecked(reason, pointer, index + i, values.get(i));
    }
  }
}
