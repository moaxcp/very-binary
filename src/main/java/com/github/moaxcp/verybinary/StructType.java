package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructType implements ComplexType<StructType>, ValueType<StructType, Struct> {

  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  @Nullable
  private final Struct constantValue;
  private final List<Type<?>> fields;

  StructType(int position, List<Type<?>> fields) {
    super(position);
    this.fields = fields;
    if (fields.stream().allMatch(t -> t.isConstant(this))) {
      constantValue = new Struct(this);
    } else {
      constantValue = null;
    }
  }

  @Override
  public boolean isConstant(@Nullable ComplexType parent) {
    return ValueType.super.isConstant(parent);
  }

  public @Nullable Struct getConstantValue() {
    return constantValue;
  }

  @Override
  public List<ValueChangeListener> getValueChangeListeners() {
    return valueChangeListeners;
  }

  public StructType addValueChangeListeners(List<ValueChangeListener> valueChangeListeners) {
    this.valueChangeListeners.addAll(valueChangeListeners);
    return this;
  }

  public StructType addValueChangeListener(ValueChangeListener listener) {
    this.valueChangeListeners.add(listener);
    return this;
  }

  List<Type<?>> getFields() {
    return fields;
  }

  @Override
  public StructType copy(int position) {
    return new StructType(position, new ArrayList<>(fields));
  }

  public <V extends Type<?>> V getType(int position) {
    return (V) fields.get(position);
  }

  @Override
  public long getAllocationLength(@Nullable ComplexType parent) {
    return fields.stream().mapToLong(f -> f.getAllocationLength(this)).sum();
  }

  public int getPositions() {
    return fields.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    long byteLength = 0;
    for (int i = 0; i < fields.size(); i++) {
      var field = fields.get(i);
      byteLength += field.getByteLength(pointer);
    }
    return byteLength;
  }

  @Override
  public boolean isFixedLength(Pointer<?, ? extends Type<?>> pointer) {
      for (int i = 0; i < fields.size(); i++) {
        var type = fields.get(i);
        if(!type.isFixedLength(pointer)) {
          return false;
        }
      }
      return true;
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer) {
    var offset = getOffset(pointer);
    return new Struct(offset, this, pointer.getByteArray());
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, Struct value) {
    checkForConstantValue();
    if (!valueChangeListeners.isEmpty()) {
      var old = new Struct(getOffset(pointer), this, pointer.getByteArray());
      if (!old.equals(value)) {
        pointer.getByteArray().replace(getOffset(pointer), getByteLength(pointer), value.getByteArray(), value.getOffset(), value.getByteLength());
        notifyValueChange(SET_VALUE, pointer, old, value);
      }
      old.removeByteArrayListener();
    } else {
      pointer.getByteArray().replace(getOffset(pointer), getByteLength(pointer), value.getByteArray(), value.getOffset(), value.getByteLength());
    }
  }

  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, Struct value) {
    if (this.isConstant(pointer.getType()) && !Objects.equals(constantValue.getByteArray(), value.getByteArray())) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + constantValue);
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isConstant(pointer.getType())) {
      pointer.getByteArray().addInt8(getOffset(pointer), constantValue.getByteArray().getAllocatedBytes());
    } else {
      for (int i = 0; i < fields.size(); i++) {
        getType(i).allocate(pointer);
      }
    }
  }

  @Override
  public String toString() {
    return "StructType{" +
        "fields=" + fields +
        ", constantValue=" + constantValue +
        ", position=" + position +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    StructType that = (StructType) o;
    return fields.equals(that.fields);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + fields.hashCode();
    return result;
  }
}
