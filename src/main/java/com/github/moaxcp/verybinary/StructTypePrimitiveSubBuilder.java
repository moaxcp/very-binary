package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.BoolList;
import com.github.moaxcp.verybinary.math.Expression;

public class StructTypePrimitiveSubBuilder<PARENT extends StructTypeBuilder<PARENT>> {
  private final PARENT structTypeBuilder;
  private final PrimitiveBuilder primitiveBuilder;

  StructTypePrimitiveSubBuilder(PARENT structTypeBuilder, int position) {
    this.structTypeBuilder = structTypeBuilder;
    primitiveBuilder = new PrimitiveBuilder().position(position);
  }

  public StructTypePrimitiveSubBuilder<PARENT> byteLengthChange(ByteLengthListener byteLengthChange) {
    primitiveBuilder.byteLengthListener(byteLengthChange);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> constant(Object constantValue) {
    primitiveBuilder.constant(constantValue);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> constant(boolean... constantValue) {
    primitiveBuilder.constant(BoolList.toBoolList(constantValue));
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> lengthField(int lengthFieldPosition) {
    primitiveBuilder.lengthExpression(Expression.variable(lengthFieldPosition));
    primitiveBuilder.arrayLengthListener(LengthListener.lengthField(lengthFieldPosition));
    ((ValueType<?, ?>) structTypeBuilder.getField(lengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendArrayListener(primitiveBuilder.getPosition()));
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> byteLengthField(int byteLengthFieldPosition) {
    primitiveBuilder.byteLengthExpression(Expression.variable(byteLengthFieldPosition));
    primitiveBuilder.byteLengthListener(ByteLengthListener.lengthField(byteLengthFieldPosition));
    ((ValueType<?, ?>) structTypeBuilder.getField(byteLengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendBytesListener(primitiveBuilder.getPosition()));
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> lengthExpression(Expression lengthExpression) {
    primitiveBuilder.lengthExpression(lengthExpression);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> byteLengthExpression(Expression byteLengthExpression) {
    primitiveBuilder.byteLengthExpression(byteLengthExpression);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> arrayLengthListener(LengthListener lengthListener) {
    primitiveBuilder.arrayLengthListener(lengthListener);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> byteLengthListener(ByteLengthListener byteLengthListener) {
    primitiveBuilder.byteLengthListener(byteLengthListener);
    return this;
  }

  public StructTypePrimitiveSubBuilder<PARENT> valueListener(ValueChangeListener valueChangeListener) {
    primitiveBuilder.valueListener(valueChangeListener);
    return this;
  }

  public PARENT bool() {
    return structTypeBuilder.type(primitiveBuilder.bool());
  }

  public PARENT int8() {
    return structTypeBuilder.type(primitiveBuilder.int8());
  }

  public PARENT uint8() {
    return structTypeBuilder.type(primitiveBuilder.uint8());
  }

  public PARENT int16() {
    return structTypeBuilder.type(primitiveBuilder.int16());
  }

  public PARENT uint16() {
    return structTypeBuilder.type(primitiveBuilder.uint16());
  }

  public PARENT int32() {
    return structTypeBuilder.type(primitiveBuilder.int32());
  }

  public PARENT uint32() {
    return structTypeBuilder.type(primitiveBuilder.uint32());
  }

  public PARENT int64() {
    return structTypeBuilder.type(primitiveBuilder.int64());
  }

  public PARENT uint64() {
    return structTypeBuilder.type(primitiveBuilder.uint64());
  }

  public PARENT float32() {
    return structTypeBuilder.type(primitiveBuilder.float32());
  }

  public PARENT float64() {
    return structTypeBuilder.type(primitiveBuilder.float64());
  }
}
