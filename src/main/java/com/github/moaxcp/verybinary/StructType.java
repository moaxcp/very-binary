package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructType extends ComplexType<StructType> implements ValueType<StructType, Struct> {
  @Nullable
  private final Expression byteLengthExpression;
  private final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  private final ByteArray constantArray;
  private final List<Type<?>> fields;

  StructType(int position, @Nullable ByteArray constant, @Nullable Expression byteLengthExpression, List<Type<?>> fields) {
    super(position);
    this.byteLengthExpression = byteLengthExpression;
    this.constantArray = constant;
    this.fields = fields;
  }

  public @Nullable Struct getConstantValue() {
    if (constantArray == null) {
      return null;
    }
    var struct = new Struct(this, constantArray);
    //constant value has its own array and should not move.
    struct.removeByteArrayListener();
    return struct;
  }

  ByteArray getConstantArray() {
    return constantArray;
  }

  @Override
  public @Nullable Expression getByteLengthExpression() {
    return byteLengthExpression;
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
    return new StructType(position, this.constantArray, byteLengthExpression, new ArrayList<>(fields));
  }

  public <V extends Type<?>> V getType(int position) {
    return (V) fields.get(position);
  }

  @Override
  public long getAllocationLength(@Nullable Type<?> parent) {
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
    checkForConstantValue(pointer, value);
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

  public boolean isConstant(Type<?> type) {
    return constantArray != null;
  }

  public boolean isConstantValue(Type<?> type) {
    return constantArray != null;
  }

  public void checkForConstantValue(Pointer<?, ? extends Type<?>> pointer, long index, Struct value) {
    if (isConstantValue(pointer.getType()) && !Objects.equals(constantArray, value.getByteArray())) {
      throw new IllegalArgumentException(getClass().getSimpleName() + " at position " + getPosition() + " is constant index: " + index + " value: " + value + " constant: " + new Struct(this, constantArray));
    }
  }

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isConstant(pointer.getType())) {
      pointer.getByteArray().addInt8(getOffset(pointer), constantArray.getAllocatedBytes());
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
        ", constantValue=" + constantArray +
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
