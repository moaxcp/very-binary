package com.github.moaxcp.verybinary;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class StructTypeBuilder<SELF extends StructTypeBuilder<SELF>> {

  final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();
  final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  @Nullable Object constant;
  final List<Type<?>> fields = new ArrayList<>();

  public SELF from(StructType structType) {
    constant(structType.getConstantValue());
    for (var type : structType.getFields()) {
      fields.add(type);
    }
    return (SELF) this;
  }

  public int fields() {
    return fields.size();
  }

  public Type<?> getField(int position) {
    return fields.get(position);
  }

  public SELF addByteLengthListeners(ByteLengthListener... listeners) {
    for(ByteLengthListener listener : listeners) {
      byteLengthListeners.add(listener);
    }
    return (SELF) this;
  }

  public SELF addByteLengthChangeListener(ByteLengthListener listener) {
    byteLengthListeners.add(listener);
    return (SELF) this;
  }

  public SELF valueListener(ValueChangeListener listener) {
    valueChangeListeners.add(listener);
    return (SELF) this;
  }

  public StructTypePrimitiveSubBuilder<SELF> primitive() {
    return new StructTypePrimitiveSubBuilder<>((SELF) this, fields.size());
  }

  public StructTypePadSubBuilder<SELF> pad() {
    return new StructTypePadSubBuilder<>((SELF) this, fields.size());
  }

  SELF type(Type<?> type) {
    if (type instanceof PadType p && p.isAlign()) {
      var previous = fields.getLast();
      fields.add(type);
      previous.addByteLengthChangeListener(ByteLengthListener.align(type.getPosition()));
    } else {
      fields.add(type);
    }
    return (SELF) this;
  }

  public SELF bool() {
    type(new BoolType(fields.size(), null));
    return (SELF) this;
  }

  public SELF bool(boolean defaultValue) {
    throw new UnsupportedOperationException("need to add defaultValue support");
  }

  public SELF boolConst(boolean constantValue) {
    type(new BoolType(fields.size(), constantValue));
    return (SELF) this;
  }

  public SELF boolArray() {
    return primitive().lengthField(fields.size() - 1).bool();
  }

  public SELF boolArray(int lengthPosition) {
    return primitive().lengthField(lengthPosition).bool();
  }

  public SELF boolArray(Expression expression) {
    return primitive().lengthExpression(expression).bool();
  }

  public SELF int8() {
    return primitive().int8();
  }

  public SELF int8Array() {
    return primitive().lengthExpression(Expression.constant(0)).int8();
  }

  public SELF int8Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).int8();
  }

  public SELF int8Array(Expression expression) {
    return primitive().lengthExpression(expression).int8();
  }

  public SELF uint8() {
    return primitive().uint8();
  }
  public SELF uint8Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).uint8();
  }

  public SELF uint8Array(Expression expression) {
    return primitive().lengthExpression(expression).uint8();
  }

  public SELF int16() {
    return primitive().int16();
  }

  public SELF int16Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).int16();
  }

  public SELF int16Array(Expression expression) {
    return primitive().lengthExpression(expression).int16();
  }

  public SELF uint16() {
    return primitive().uint16();
  }

  public SELF uint16Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).uint16();
  }

  public SELF uint16Array(Expression expression) {
    return primitive().lengthExpression(expression).uint16();
  }

  public SELF int32() {
    return primitive().int32();
  }

  public SELF int32Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).int32();
  }

  public SELF int32Array(Expression expression) {
    return primitive().lengthExpression(expression).int32();
  }
  public SELF uint32() {
    return primitive().uint32();
  }

  public SELF uint32Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).uint32();
  }

  public SELF uint32Array(Expression expression) {
    return primitive().lengthExpression(expression).uint32();
  }

  public SELF int64() {
    return primitive().int64();
  }

  public SELF int64Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).int64();
  }

  public SELF int64Array(Expression expression) {
    return primitive().lengthExpression(expression).int64();
  }

  public SELF uint64() {
    return primitive().uint64();
  }
  public SELF uint64Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).uint64();
  }

  public SELF uint64Array(Expression expression) {
    return primitive().lengthExpression(expression).uint64();
  }

  public SELF float32() {
    return primitive().float32();
  }

  public SELF float32Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).float32();
  }

  public SELF float32Array(Expression expression) {
    return primitive().lengthExpression(expression).float32();
  }

  public SELF float64() {
    return primitive().float64();
  }

  public SELF float64Array(int lengthPosition) {
    return primitive().lengthField(lengthPosition).float64();
  }

  public SELF float64Array(Expression expression) {
    return primitive().lengthExpression(expression).float64();
  }

  public SELF pad(long length) {
    return pad().length(length).pad();
  }

  public SELF align(long length) {
    return pad().length(length).align();
  }

  public ChildStructTypeBuilder<SELF> struct() {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size());
  }

  public SELF struct(StructType type) {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size()).from(type).end();
  }

  public ChildStructTypeBuilder<SELF> structArray(int lengthPosition) {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size()).lengthField(lengthPosition);
  }

  public SELF structArray(StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .constant(type.getConstantValue());

    for (var field : type.getFields()) {
      builder.type(field);
    }
    return builder.end();
  }

  public SELF structArray(int lengthPosition, StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .lengthField(lengthPosition)
        .constant(type.getConstantValue());

    for (var field : type.getFields()) {
      builder.type(field);
    }
    return builder.end();
  }

  public ChildStructTypeBuilder<SELF> structArray(Expression lengthExpression) {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size()).lengthExpression(lengthExpression);
  }

  public SELF structArray(Expression lengthExpression, StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .lengthExpression(lengthExpression)
        .constant(type.getConstantValue());

    for (var field : type.getFields()) {
      builder.type(field);
    }
    return builder.end();
  }

  public SELF constant(Struct constant) {
    if(constant == null) {
      return (SELF) this;
    }
    this.constant = constant.getByteArray();
    return (SELF) this;
  }

  public SELF constant(ByteArray constant) {
    this.constant = constant;
    return (SELF) this;
  }

  public StructListType toStructListType() {
    return new StructListType(-1, (ByteArray) constant, lengthExpression, byteLengthExpression, toStructType())
        .addByteLengthChangeListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners);
  }

  public StructType toStructType() {
    return new StructType(-1, fields)
        .addByteLengthChangeListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners);
  }
}
