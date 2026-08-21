package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public final class Multiply implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  Multiply(ArithmeticExpression... expressions) {
    this(List.of(expressions));
  }

  Multiply(List<? extends ArithmeticExpression> expressions) {
    if (expressions.size() < 2) {
      throw new IllegalArgumentException("Multiply must have at least two expressions");
    }

    this.expressions = List.copyOf(expressions);
  }

  public static Multiply multiply(ArithmeticExpression... expressions) {
    return new Multiply(expressions);
  }

  public static Multiply multiply(List<ArithmeticExpression> expressions) {
    return new Multiply(expressions);
  }

  public static ArithmeticExpression distribute(Multiply multiply) {
    if (multiply.expressions().stream().noneMatch(e -> e instanceof ArithmeticExpression)) {
      return multiply;
    }
    var first = multiply.expressions().getFirst();
    var partOfTerm = new ArrayList<ArithmeticExpression>();
    var newExpressions = new ArrayList<ArithmeticExpression>();
    for (int i = 1; i < multiply.expressions().size(); i++) {
      Expression expr = multiply.expressions().get(i);
      switch (expr) {
        case Sum sum -> newExpressions.add(new Sum(sum.expressions().stream()
            .map(term -> multiplyBy(first, term))
            .toList()));
        case Subtract sub -> newExpressions.add(new Subtract(sub.expressions().stream()
            .map(term -> multiplyBy(first, term))
            .toList()));
        case Multiply m -> newExpressions.add(distribute(multiply(Stream.concat(Stream.of(first), m.expressions.stream()).toList())));
        case Divide d -> newExpressions.add(Divide.distribute(new Divide(d.expressions().stream()
            .map(term -> multiplyBy(first, term))
            .toList())));
        case ArithmeticValue constant -> partOfTerm.add(constant);
        case ValueOf valueOf -> partOfTerm.add(valueOf);
        case LengthOf lengthOf -> partOfTerm.add(lengthOf);
        case ByteLengthOf byteLengthOf -> partOfTerm.add(byteLengthOf);
        case ByteLengthOfBasicElement byteLengthOfBasicElement -> partOfTerm.add(byteLengthOfBasicElement);
        case SameExpression sameExpression -> {
        }
        case GreaterThanExpression greaterThanExpression -> {
        }
        case GreaterThanOrEqualExpression greaterThanOrEqualExpression -> {
        }
        case LessThanExpression lessThanExpression -> {
        }
        case LessThanOrEqualExpression lessThanOrEqualExpression -> {
        }
        case NotSameExpression notSameExpression -> {
        }
        case ValueOfBit valueOfBit -> {
        }
        case StructValue structValue -> {
        }
        case StructVariable structVariable -> {
        }
        case Variable variable -> {
        }
      }
    }
    if (!partOfTerm.isEmpty()) {
      partOfTerm.add(0, first);
      newExpressions.addAll(0, partOfTerm);
    }
    if (newExpressions.size() == 1) {
      return newExpressions.getFirst();
    } else {
      return multiply(newExpressions);
    }
  }

  static ArithmeticExpression multiplyBy(ArithmeticExpression first, ArithmeticExpression second) {
    return switch(second) {
      case Sum sum -> new Sum(sum.expressions().stream().map(e -> multiplyBy(first, e)).toList());
      case Subtract subtract -> new Subtract(subtract.expressions().stream().map(e -> multiplyBy(first, e)).toList());
      case Multiply multiply -> distribute(new Multiply(Stream.concat(Stream.of(first), multiply.expressions().stream()).toList()));
      case Divide divide -> Divide.distribute(new Divide(divide.expressions().stream().map(e -> multiplyBy(first, e)).toList()));
      case ArithmeticValue value -> new Multiply(first, value);
      case ValueOf valueOf -> new Multiply(List.of(first, valueOf));
      case LengthOf lengthOf -> new Multiply(List.of(first, lengthOf));
      case ByteLengthOf byteLengthOf -> new Multiply(List.of(first, byteLengthOf));
      case ByteLengthOfBasicElement byteLengthOfBasicElement -> new Multiply(List.of(first, byteLengthOfBasicElement));
      case SameExpression sameExpression -> new Multiply(first, sameExpression);
      case GreaterThanExpression greaterThanExpression -> multiply(first, greaterThanExpression);
      case GreaterThanOrEqualExpression greaterThanOrEqualExpression -> multiply(first, greaterThanOrEqualExpression);
      case LessThanExpression lessThanExpression -> multiply(first, lessThanExpression);
      case LessThanOrEqualExpression lessThanOrEqualExpression -> multiply(first, lessThanOrEqualExpression);
      case NotSameExpression notSameExpression -> multiply(first, notSameExpression);
      case ValueOfBit valueOfBit -> multiply(first, valueOfBit);
      case Variable variable -> multiply(first, variable);
    };
  }

  static ArithmeticExpression simplify(Multiply multiply) {
    var constants = new ArrayList<ArithmeticValue>();
    var newExpressions = new ArrayList<ArithmeticExpression>();
    for (var expression : multiply.expressions()) {
      if (expression instanceof ArithmeticValue e) {
        constants.add(e);
      } else if (expression instanceof Sum sum) {
        newExpressions.add(Sum.simplify(sum));
      } else if (expression instanceof Subtract sub) {
        newExpressions.add(Subtract.simplify(sub));
      } else if (expression instanceof Multiply m) {
        newExpressions.add(Multiply.simplify(m));
      } else if (expression instanceof Divide d) {
        newExpressions.add(Divide.simplify(d));
      } else {
        newExpressions.add(expression);
      }
    }
    var constant = constants.stream().reduce(Int8Value.int8Value(1), ArithmeticValue::multiply);
    newExpressions.add(constant);
    if (newExpressions.size() == 1) {
      return newExpressions.getFirst();
    } else {
      return multiply(newExpressions);
    }
  }

  public List<ArithmeticExpression> expressions() {
    return expressions;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return expressions.stream().allMatch(e -> e.isConstant(parent));
  }

  @Override
  public ArithmeticValue constantValue(ComplexType<?> parent) {
    var result = (ArithmeticValue) expressions.get(0).constantValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.multiply((ArithmeticValue) expressions.get(i).constantValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    var result = (ArithmeticValue) expressions.get(0).defaultValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.multiply((ArithmeticValue) expressions.get(i).defaultValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    var result = (ArithmeticValue) expressions.get(0).evaluate(pointer);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.multiply((ArithmeticValue) expressions.get(i).evaluate(pointer));
    }
    return result;
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" * ");
    for (Expression expression : expressions) {
      if (expression instanceof Sum || expression instanceof Subtract || expression instanceof Divide) {
        joiner.add("(" + expression + ")");
      } else {
        joiner.add(expression.toString());
      }
    }
    return joiner.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    var other = ((Multiply) o).expressions();
    if (expressions == other) return true;
    if (expressions.size() != other.size()) return false;
    Map<Expression, Long> map1 = expressions.stream().collect(groupingBy(e -> e, counting()));
    Map<Expression, Long> map2 = other.stream().collect(groupingBy(e -> e, counting()));

    return map1.equals(map2);
  }

  @Override
  public int hashCode() {
    return expressions.stream()
      .mapToInt(Object::hashCode)
      .sum();
  }
}
