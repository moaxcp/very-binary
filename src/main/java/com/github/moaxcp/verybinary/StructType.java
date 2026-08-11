package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public final class StructType extends ValueType<StructType, Struct> implements ComplexType<StructType> {

  private final List<Type<?>> fields = new ArrayList<>();

  StructType(int position, @Nullable ComplexType<?> parent, @Nullable Struct constantValue, List<Type<?>> fields) {
    super(position, parent, constantValue);
    for(int i = 0; i < fields.size(); i++) {
      this.fields.add(((AbstractType<?>) fields.get(i)).setParent(this));
    }
  }

  StructType(int position, @Nullable ComplexType<?> parent, ByteArray constantValue, List<Type<?>> fields) {
    super(position, parent, null);
    for(int i = 0; i < fields.size(); i++) {
      this.fields.add(((AbstractType<?>) fields.get(i)).setParent(this));
    }
    this.constantValue = new Struct(this, constantValue);
  }

  @Override
  public StructType copy(int position, @Nullable ComplexType<?> parent) {
    return new StructType(position, parent, constantValue, new ArrayList<>(fields));
  }

  public List<Type<?>> getTypes() {
    return fields;
  }

  public <V extends Type<?>> V getType(int position) {
    return (V) fields.get(position);
  }

  @Override
  public long getAllocationByteLength() {
    return fields.stream().mapToLong(Type::getAllocationByteLength).sum();
  }

  public int getPositions() {
    return fields.size();
  }

  @Override
  public long getByteLength(Pointer<?, ? extends Type<?>> pointer) {
    long byteLength = 0;
    for (int i = 0; i < fields.size(); i++) {
      var field = fields.get(i);
      if (field instanceof StructType st) {
        var s = new Struct(getOffset(pointer), st.getOffset(pointer), st, pointer.getByteArray());
        s.removeByteArrayListener();
        byteLength += st.getByteLength(s);
      } else {
        byteLength += field.getByteLength(pointer);
      }
    }
    return byteLength;
  }

  @Override
  public boolean isFixedByteLength() {
      for (int i = 0; i < fields.size(); i++) {
        var type = fields.get(i);
        if(!type.isFixedByteLength()) {
          return false;
        }
      }
      return true;
  }

  @Override
  public Struct get(Pointer<?, ? extends Type<?>> pointer) {
    var offset = getOffset(pointer);
    return new Struct(pointer.getParentOffset(), offset, this, pointer.getByteArray());
  }

  @Override
  protected void setUnchecked(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Struct value) {
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

  @Override
  public void allocate(Pointer<?, ? extends Type<?>> pointer) {
    if (isConstant()) {
      pointer.getByteArray().shiftBytesFor(getOffset(pointer), constantValue.getByteLength());
      pointer.getByteArray().replace(getOffset(pointer), constantValue.getByteLength(), constantValue.getByteArray(), constantValue.getOffset(), constantValue.getByteLength());
    } else {
      for (var field : fields) {
        if (field instanceof StructType st) {
          var struct = new Struct(false, pointer.getOffset(), field.getOffset(pointer), st, pointer.getByteArray());
          struct.removeByteArrayListener();
        } else {
          field.allocate(pointer);
        }
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
