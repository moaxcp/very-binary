package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.StructList;
import com.github.moaxcp.verybinary.math.ArithmeticExpression;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.verybinary.math.Int64Value.int64Value;

public abstract class StructTypeBuilder<SELF extends StructTypeBuilder<SELF>> {

  ArithmeticExpression lengthExpression;
  final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();
  final List<ValueChangeListener> valueChangeListeners = new ArrayList<>();
  @Nullable Object constant;
  final List<Type<?>> fields = new ArrayList<>();

  public SELF from(StructType structType) {
    constant(structType.getConstantValue());
    for (var type : structType.getTypes()) {
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

  public SELF lengthExpression(ArithmeticExpression lengthExpression) {
    this.lengthExpression = lengthExpression;
    return (SELF) this;
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

  public StructTypeBasicSubBuilder<SELF> basic() {
    return new StructTypeBasicSubBuilder<>((SELF) this, fields.size());
  }

  public StructTypePadSubBuilder<SELF> pad() {
    return new StructTypePadSubBuilder<>((SELF) this, fields.size());
  }

  SELF type(Type<?> type) {
    if (type instanceof PadType p && p.isAlign()) {
      var previous = fields.getLast();
      fields.add(type);
      ((AbstractType<?>) previous).addByteLengthListeners(List.of(ByteLengthListener.align(type.getPosition())));
    } else {
      fields.add(type);
    }
    return (SELF) this;
  }

  public SELF bool() {
    type(new BoolType(fields.size(), null, null));
    return (SELF) this;
  }

  public SELF bool(boolean defaultValue) {
    throw new UnsupportedOperationException("need to add defaultValue support");
  }

  public SELF boolConst(boolean constantValue) {
    type(new BoolType(fields.size(), null, constantValue));
    return (SELF) this;
  }

  public SELF boolList() {
    return basic().lengthField(fields.size() - 1).bool();
  }

  public SELF boolList(int lengthPosition) {
    return basic().lengthField(lengthPosition).bool();
  }

  public SELF boolList(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).bool();
  }

  public SELF int8() {
    return basic().int8();
  }

  public SELF int8List() {
    return basic().lengthExpression(int64Value(0)).int8();
  }

  public SELF int8List(int lengthPosition) {
    return basic().lengthField(lengthPosition).int8();
  }

  public SELF int8List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).int8();
  }

  public SELF uint8() {
    return basic().uint8();
  }
  public SELF uint8List(int lengthPosition) {
    return basic().lengthField(lengthPosition).uint8();
  }

  public SELF uint8List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).uint8();
  }

  public SELF int16() {
    return basic().int16();
  }

  public SELF int16List(int lengthPosition) {
    return basic().lengthField(lengthPosition).int16();
  }

  public SELF int16List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).int16();
  }

  public SELF uint16() {
    return basic().uint16();
  }

  public SELF uint16List(int lengthPosition) {
    return basic().lengthField(lengthPosition).uint16();
  }

  public SELF uint16List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).uint16();
  }

  public SELF int32() {
    return basic().int32();
  }

  public SELF int32List(int lengthPosition) {
    return basic().lengthField(lengthPosition).int32();
  }

  public SELF int32List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).int32();
  }
  public SELF uint32() {
    return basic().uint32();
  }

  public SELF uint32List(int lengthPosition) {
    return basic().lengthField(lengthPosition).uint32();
  }

  public SELF uint32List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).uint32();
  }

  public SELF int64() {
    return basic().int64();
  }

  public SELF int64List(int lengthPosition) {
    return basic().lengthField(lengthPosition).int64();
  }

  public SELF int64List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).int64();
  }

  public SELF uint64() {
    return basic().uint64();
  }
  public SELF uint64List(int lengthPosition) {
    return basic().lengthField(lengthPosition).uint64();
  }

  public SELF uint64List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).uint64();
  }

  public SELF float32() {
    return basic().float32();
  }

  public SELF float32List(int lengthPosition) {
    return basic().lengthField(lengthPosition).float32();
  }

  public SELF float32List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).float32();
  }

  public SELF float64() {
    return basic().float64();
  }

  public SELF float64List(int lengthPosition) {
    return basic().lengthField(lengthPosition).float64();
  }

  public SELF float64List(ArithmeticExpression expression) {
    return basic().lengthExpression(expression).float64();
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

  public ChildStructTypeBuilder<SELF> structList(int lengthPosition) {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size()).lengthField(lengthPosition);
  }

  public SELF structList(StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .constant(type.getConstantValue());

    for (var field : type.getTypes()) {
      builder.type(field);
    }
    return builder.end();
  }

  public SELF structList(int lengthPosition, StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .lengthField(lengthPosition)
        .constant(type.getConstantValue());

    for (var field : type.getTypes()) {
      builder.type(field);
    }
    return builder.end();
  }

  public SELF structList(ArithmeticExpression lengthExpression) {
    return new ChildStructTypeBuilder<>((SELF) this, fields.size()).lengthExpression(lengthExpression).end();
  }

  public SELF structList(ArithmeticExpression lengthExpression, StructType type) {
    var builder = new ChildStructTypeBuilder<>((SELF) this, fields.size())
        .lengthExpression(lengthExpression)
        .constant(type.getConstantValue());

    for (var field : type.getTypes()) {
      builder.type(field);
    }
    return builder.end();
  }

  public SELF structList(StructListType structListType) {
    fields.add(structListType.copy(fields.size(), null));
    return (SELF) this;
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
    return new StructListType(-1, null, (StructList) constant, lengthExpression, toStructType())
        .addByteLengthListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners);
  }

  public StructType toStructType() {
    if (constant instanceof ByteArray) {
      return new StructType(-1, null, (ByteArray) constant, fields)
          .addByteLengthListeners(byteLengthListeners)
          .addValueChangeListeners(valueChangeListeners);
    }
    return new StructType(-1, null, (Struct) constant, fields)
        .addByteLengthListeners(byteLengthListeners)
        .addValueChangeListeners(valueChangeListeners);
  }
}
