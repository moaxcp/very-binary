package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.*;

import java.util.ArrayList;
import java.util.List;

public sealed interface Expression permits Constant, Divide, LengthOf, Multiply, Subtract, Sum, Variable {

  static Constant constant(long value) {
    return new Constant(value);
  }

  static Variable variable(int position) {
    return new Variable(position);
  }

  static LengthOf basicElementLengthOf(int position) {
    return new LengthOf(position);
  }

  static Sum sum(Expression... expressions) {
    return new Sum(expressions);
  }

  static Subtract subtract(Expression... expressions) {
    return new Subtract(expressions);
  }

  static Multiply multiply(Expression... expressions) {
    return new Multiply(expressions);
  }

  static Divide divide(Expression... expressions) {
    return new Divide(expressions);
  }

  default List<Variable> findVariables(int position) {
    var variables = new ArrayList<Variable>();
    switch (this) {
      case LengthOf ignored -> {}
      case BasicElementLengthOf ignored -> {}
      case Constant ignored -> {}
      case Variable v -> {
        if (v.position() == position) {
          variables.add(v);
        }
      }
      case Sum sum -> variables.addAll(sum.findVariables(position));
      case Subtract sub -> variables.addAll(sub.findVariables(position));
      case Multiply mul -> variables.addAll(mul.findVariables(position));
      case Divide div -> variables.addAll(div.findVariables(position));
    }
    return variables;
  }

  boolean isConstant(ComplexType<?> parent);

  long constantValue(ComplexType<?> parent);

  long defaultValue(ComplexType<?> parent);

  long evaluate(Pointer<?, ? extends Type<?>> pointer);
}
