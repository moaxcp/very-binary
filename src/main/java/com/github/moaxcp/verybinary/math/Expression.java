package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.ArrayList;
import java.util.List;

public sealed interface Expression<T extends Value<T>> permits ArithmeticExpression, MultiExpression, StructVariable, Value {

  default List<ValueOf> findVariables(int position) {
    var variables = new ArrayList<ValueOf>();
    switch (this) {
      case LengthOf ignored -> {}
      case ByteLengthOf ignored -> {}
      case ByteLengthOfBasicElement ignored -> {}
      case Value ignored -> {}
      case ValueOf v -> {
        if (v.position() == position) {
          variables.add(v);
        }
      }
      case Sum sum -> variables.addAll(sum.findVariables(position));
      case Subtract sub -> variables.addAll(sub.findVariables(position));
      case Multiply mul -> variables.addAll(mul.findVariables(position));
      case Divide div -> variables.addAll(div.findVariables(position));
      case MultiExpression multiExpression -> {
      }
      case StructVariable structVariable -> {
      }
      case ValueOfBit valueOfBit -> {
      }
    }
    return variables;
  }

  static boolean likeTerms(Expression first, Expression second) {
    return switch (first) {
      case LengthOf ignored -> first.equals(second);
      case ByteLengthOf ignored -> first.equals(second);
      case ByteLengthOfBasicElement ignored -> first.equals(second);
      case Value ignored -> true;
      case ValueOf ignored -> first.equals(second);
      case Multiply mul -> false;
      case Divide div -> false;
      case Sum sum -> false;
      case Subtract sub -> false;
      case SameExpression sameExpression -> false;
      case StructVariable structVariable -> false;
      case GreaterThanExpression greaterThanExpression -> false;
      case GreaterThanOrEqualExpression greaterThanOrEqualExpression -> false;
      case LessThanExpression lessThanExpression -> false;
      case LessThanOrEqualExpression lessThanOrEqualExpression -> false;
      case NotSameExpression notSameExpression -> false;
      case ValueOfBit valueOfBit -> false;
    };
  }

  boolean isConstant(ComplexType<?> parent);

  T constantValue(ComplexType<?> parent);

  T defaultValue(ComplexType<?> parent);

  T evaluate(Pointer<?, ? extends Type<?>> pointer);
}
