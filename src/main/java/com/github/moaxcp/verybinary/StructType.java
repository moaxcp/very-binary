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
    return new Struct(this, constantArray);
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

  public long getFieldByteLength(Pointer<?, ? extends Type<?>> pointer, int position, long index) {
    return ((ValueType) fields.get(position)).getByteLength(pointer, index);
  }

  public long getFieldByteLength(Pointer<?, ? extends Type<?>> pointer, int position, long index, long length) {
    return ((ValueType) fields.get(position)).getByteLength(pointer, index, length);
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
    if (isArray()) {

    } else {
      if (isConstant(pointer.getType())) {
        pointer.getByteArray().addInt8(getOffset(pointer), constantArray.getAllocatedBytes());
      } else {
        for (int i = 0; i < fields.size(); i++) {
          getType(i).allocate(pointer);
        }
      }
    }
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        if (isConstantValue(pointer.getType())) {
          pointer.getByteArray().addInt8(getOffset(pointer), constantArray.getAllocatedBytes());
        } else {
          var type = copyForArrayElement();
          var struct = new Struct(true, getOffset(pointer, index), type, pointer.getByteArray());
          struct.removeByteArrayListener();
          for (int i = 0; i < fields.size(); i++) {
            type.getType(i).allocate(struct);
          }
        }
      });
    });
  }

  @Override
  public void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, length, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var type = copyForArrayElement();
        for (long i = 0; i < length; i++) {
          if (isConstantValue(pointer.getType())) {
            pointer.getByteArray().addInt8(getOffset(pointer), constantArray.getAllocatedBytes());
          } else {
            var struct = new Struct(true, getOffset(pointer, index + i), type, pointer.getByteArray());
            struct.removeByteArrayListener();
            for (int j = 0; j < fields.size(); j++) {
              type.getType(j).allocate(struct);
            }
          }
        }
      });
    });
  }

  @Override
  public String toString() {
    return "StructType{" +
        "fields=" + fields +
        ", lengthExpression=" + lengthExpression +
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
