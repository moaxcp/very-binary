package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.StructList;

import java.util.ArrayList;
import java.util.List;

public class ChildStructTypeBuilder<PARENT extends StructTypeBuilder<?>> extends StructTypeBuilder<ChildStructTypeBuilder<PARENT>> {
  private final PARENT parent;
  private int position;
  final List<LengthListener> lengthListeners = new ArrayList<>();

  ChildStructTypeBuilder(PARENT structTypeBuilder, int position) {
    this.parent = structTypeBuilder;
    this.position(position);
  }

  public ChildStructTypeBuilder<PARENT> position(int position) {
    this.position = position;
    return this;
  }

  public ChildStructTypeBuilder<PARENT> byteLengthListener(ByteLengthListener listener) {
    byteLengthListeners.add(listener);
    return this;
  }

  public ChildStructTypeBuilder<PARENT> arrayLengthListener(LengthListener listener) {
    lengthListeners.add(listener);
    return this;
  }

  public ChildStructTypeBuilder<PARENT> byteLengthField(int byteLengthFieldPosition) {
    this.byteLengthListeners.add(ByteLengthListener.lengthField(byteLengthFieldPosition));
    ((ValueType<?, ?>) parent.fields.get(byteLengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendBytesListener(byteLengthFieldPosition));
    return this;
  }

  public ChildStructTypeBuilder<PARENT> lengthField(int lengthFieldPosition) {
    this.lengthExpression = Expression.valueOf(lengthFieldPosition);
    this.lengthListeners.add(LengthListener.lengthField(lengthFieldPosition));
    ((ValueType<?, ?>) parent.fields.get(lengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendArrayListener(position));
    return this;
  }

  public ChildStructTypeBuilder<PARENT> structArray(int lengthPosition, StructType type) {
    lengthField(lengthPosition);
    return structArray(type);
  }

  public StructListType toStructListType() {
    return new StructListType(position, null, (StructList) constant, lengthExpression, toStructType())
        .addByteLengthListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners)
        .addLengthListeners(lengthListeners);
  }

  public StructType toStructType() {
    return new StructType(position, null, (Struct) constant, fields)
        .addByteLengthListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners);
  }

  public PARENT end() {
    if (lengthExpression != null) {
      parent.type(toStructListType());
    } else {
      parent.type(toStructType());
    }
    return parent;
  }
}
