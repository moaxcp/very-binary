package com.github.moaxcp.verybinary;

import java.util.ArrayList;
import java.util.List;

public class ChildStructTypeBuilder<PARENT extends StructTypeBuilder<?>> extends StructTypeBuilder<ChildStructTypeBuilder<PARENT>> {
  private final PARENT parent;
  private int position;
  final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();
  final List<ArrayLengthListener> arrayLengthListeners = new ArrayList<>();

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

  public ChildStructTypeBuilder<PARENT> arrayLengthListener(ArrayLengthListener listener) {
    arrayLengthListeners.add(listener);
    return this;
  }

  public ChildStructTypeBuilder<PARENT> byteLengthField(int byteLengthFieldPosition) {
    this.byteLengthExpression = Expression.valueOf(byteLengthFieldPosition);
    this.byteLengthListeners.add(ByteLengthListener.lengthField(byteLengthFieldPosition));
    ((ValueType<?, ?>) parent.fields.get(byteLengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendBytesListener(byteLengthFieldPosition));
    return this;
  }

  public ChildStructTypeBuilder<PARENT> lengthField(int lengthFieldPosition) {
    this.lengthExpression = Expression.valueOf(lengthFieldPosition);
    this.arrayLengthListeners.add(ArrayLengthListener.lengthField(lengthFieldPosition));
    ((ValueType<?, ?>) parent.fields.get(lengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendArrayListener(lengthFieldPosition));
    return this;
  }

  public StructType toStructType() {
    var type = new StructType(position, constant, lengthExpression, byteLengthExpression, fields);
    type.addByteLengthChangeListeners(byteLengthListeners);
    type.addArrayLengthChangeListeners(arrayLengthListeners);
    type.addValueChangeListeners(valueChangeListeners);
    return type;
  }

  public PARENT end() {
    parent.type(toStructType());
    return parent;
  }
}
