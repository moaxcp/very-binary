package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructType extends ValueType<StructType, Struct> {

  private final List<Type<?>> fields;

  StructType(int position, @Nullable Struct constant, @Nullable Expression lengthExpression, @Nullable Expression byteLengthExpression, List<Type<?>> fields) {
    super(position, constant, lengthExpression, byteLengthExpression);
    this.fields = fields;
  }

  @Override
  public StructType copy(int position) {
    return new StructType(position, this.constantValue, this.lengthExpression, byteLengthExpression, new ArrayList<>(fields));
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
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer, long index) {
    long byteLength = 0;
    var length = getArrayLength(pointer);
    for (long i = 0; i < length; i++) {
      var struct = new Struct(getOffset(pointer, i), this, pointer.getByteArray());
      for (int j = 0; j < fields.size(); j++) {
        var field = fields.get(j);
        byteLength += field.getByteLength(struct);
      }
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

  public long getFieldByteLength(Pointer<?, ? extends Type<?>> pointer, int position) {
    return fields.get(position).getByteLength(pointer);
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
    return lengthExpression == null || lengthExpression.isConstant(pointer.getType());
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer, long index) {
    var offset = getOffset(pointer, index);
    return new Struct(offset, this, pointer.getByteArray());
  }
  
  @Override
  public Struct[] getArray(Pointer<?, ? extends Type<?>> pointer) {
    var length = getArrayLength(pointer);
    var result = new Struct[Math.toIntExact(length)];
    for (long i = 0; i < length; i++) {
      result[Math.toIntExact(i)] = get(pointer, i);
    }
    return result;
  }

  @Override
  public Struct[] getArray(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    var result = new Struct[Math.toIntExact(length)];
    for (long i = 0; i < length; i++) {
      result[Math.toIntExact(i)] = get(pointer, index + i);
    }
    return result;
  }
  
  @Override
  public List<Struct> getList(Pointer<?, ? extends Type<?>> pointer) {
    var length = getArrayLength(pointer);
    var result = new ArrayList<Struct>();
    for (long i = 0; i < length; i++) {
      result.add(get(pointer, i));
    }
    return result;
  }

  @Override
  public List<Struct> getList(Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    var result = new ArrayList<Struct>();
    for (long i = 0; i < length; i++) {
      result.add(get(pointer, index + i));
    }
    return result;
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, Struct value) {
    if (!valueChangeListeners.isEmpty()) {
      var old = new Struct(getOffset(pointer, index), this, pointer.getByteArray());
      pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index), value.getByteArray(), value.getOffset(), value.getByteLength());
      notifyValueChange(SET_VALUE, pointer, index, old, value);
    }
    pointer.getByteArray().replace(getOffset(pointer, index), getByteLength(pointer, index), value.getByteArray(), value.getOffset(), value.getByteLength());
  }

  @Override
  public void set(Pointer<?, ? extends Type<?>> pointer, long index, Struct... values) {
    for (int i = 0; i < values.length; i++) {
      set(pointer, index + i, values[i]);
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
    if (isArray()) {
      var length = getArrayLength(pointer);
      for (long $ = 0; $ < length; $++) {
        for (int i = 0; i < fields.size(); i++) {
          var type = fields.get(i);
          if (type instanceof StructType structType) {
            new Struct(false, type.getOffset(pointer), structType, pointer.getByteArray());
          } else {
            fields.get(i).allocate(pointer);
          }
        }
      }
    }
    for (int i = 0; i < fields.size(); i++) {
      var type = fields.get(i);
      if (type instanceof StructType structType) {
        new Struct(false, type.getOffset(pointer), structType, pointer.getByteArray());
      } else {
        fields.get(i).allocate(pointer);
      }
    }
  }

  @Override
  protected void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        var struct = new Struct(false, getOffset(pointer, index), this, pointer.getByteArray());
        for (int i = 0; i < fields.size(); i++) {
          struct.getType(i).allocate(pointer);
        }
      });
    });
  }

  @Override
  void allocate(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, long length) {
    callWithArrayLengthChange(reason, pointer, 1, () -> {
      callWithByteLengthChange(reason, pointer, () -> {
        checkIndexAllocate(pointer, index);
        for (long i = 0; i < length; i++) {
          var struct = new Struct(false, getOffset(pointer, index + i), this, pointer.getByteArray());
          for (int j = 0; j < fields.size(); j++) {
            struct.getType(j).allocate(pointer);
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
