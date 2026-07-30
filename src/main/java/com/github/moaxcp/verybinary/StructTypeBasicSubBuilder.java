package com.github.moaxcp.verybinary;

import com.github.moaxcp.verybinary.list.BoolList;
import com.github.moaxcp.verybinary.math.Expression;

import static com.github.moaxcp.verybinary.math.Variable.variable;

public class StructTypeBasicSubBuilder<PARENT extends StructTypeBuilder<PARENT>> {
  private final PARENT structTypeBuilder;
  private final BasicTypeBuilder basicTypeBuilder;

  StructTypeBasicSubBuilder(PARENT structTypeBuilder, int position) {
    this.structTypeBuilder = structTypeBuilder;
    basicTypeBuilder = new BasicTypeBuilder().position(position);
  }

  public StructTypeBasicSubBuilder<PARENT> byteLengthChange(ByteLengthListener byteLengthChange) {
    basicTypeBuilder.byteLengthListener(byteLengthChange);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> constant(Object constantValue) {
    basicTypeBuilder.constant(constantValue);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> constant(boolean... constantValue) {
    basicTypeBuilder.constant(BoolList.toBoolList(constantValue));
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> lengthField(int lengthFieldPosition) {
    basicTypeBuilder.lengthExpression(variable(lengthFieldPosition));
    basicTypeBuilder.arrayLengthListener(LengthListener.lengthField(lengthFieldPosition));
    ((ValueType<?, ?>) structTypeBuilder.getField(lengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendArrayListener(basicTypeBuilder.getPosition()));
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> byteLengthField(int byteLengthFieldPosition) {
    basicTypeBuilder.byteLengthExpression(variable(byteLengthFieldPosition));
    basicTypeBuilder.byteLengthListener(ByteLengthListener.lengthField(byteLengthFieldPosition));
    ((ValueType<?, ?>) structTypeBuilder.getField(byteLengthFieldPosition)).addValueChangeListener(ValueChangeListener.extendBytesListener(basicTypeBuilder.getPosition()));
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> lengthExpression(Expression lengthExpression) {
    basicTypeBuilder.lengthExpression(lengthExpression);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> byteLengthExpression(Expression byteLengthExpression) {
    basicTypeBuilder.byteLengthExpression(byteLengthExpression);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> arrayLengthListener(LengthListener lengthListener) {
    basicTypeBuilder.arrayLengthListener(lengthListener);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> byteLengthListener(ByteLengthListener byteLengthListener) {
    basicTypeBuilder.byteLengthListener(byteLengthListener);
    return this;
  }

  public StructTypeBasicSubBuilder<PARENT> valueListener(ValueChangeListener valueChangeListener) {
    basicTypeBuilder.valueListener(valueChangeListener);
    return this;
  }

  public PARENT bool() {
    return structTypeBuilder.type(basicTypeBuilder.bool());
  }

  public PARENT int8() {
    return structTypeBuilder.type(basicTypeBuilder.int8());
  }

  public PARENT uint8() {
    return structTypeBuilder.type(basicTypeBuilder.uint8());
  }

  public PARENT int16() {
    return structTypeBuilder.type(basicTypeBuilder.int16());
  }

  public PARENT uint16() {
    return structTypeBuilder.type(basicTypeBuilder.uint16());
  }

  public PARENT int32() {
    return structTypeBuilder.type(basicTypeBuilder.int32());
  }

  public PARENT uint32() {
    return structTypeBuilder.type(basicTypeBuilder.uint32());
  }

  public PARENT int64() {
    return structTypeBuilder.type(basicTypeBuilder.int64());
  }

  public PARENT uint64() {
    return structTypeBuilder.type(basicTypeBuilder.uint64());
  }

  public PARENT float32() {
    return structTypeBuilder.type(basicTypeBuilder.float32());
  }

  public PARENT float64() {
    return structTypeBuilder.type(basicTypeBuilder.float64());
  }
}
