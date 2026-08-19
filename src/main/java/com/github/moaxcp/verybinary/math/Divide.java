package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public final class Divide implements ArithmeticExpression, MultiExpression<ArithmeticExpression, ArithmeticValue> {

  private final List<ArithmeticExpression> expressions;

  Divide(ArithmeticExpression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public Divide(List<? extends ArithmeticExpression> expressions) {
    if (expressions == null || expressions.size() < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.copyOf(expressions);
  }

  public static Divide divide(ArithmeticExpression... expressions) {
    return new Divide(expressions);
  }

  public static Divide divide(List<? extends ArithmeticExpression> expressions) {
    return new Divide(expressions);
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
    var result = expressions.get(0).constantValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.divide(expressions.get(i).constantValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue defaultValue(ComplexType<?> parent) {
    var result = expressions.get(0).defaultValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.divide(expressions.get(i).defaultValue(parent));
    }
    return result;
  }

  @Override
  public ArithmeticValue evaluate(Pointer<?, ? extends Type<?>> pointer) {
    var result = expressions.get(0).evaluate(pointer);
    for (int i = 1; i < expressions.size(); i++) {
      result = result.divide(expressions.get(i).evaluate(pointer));
    }
    return result;
  }

  public static ArithmeticExpression distribute(Divide divide) {
    if (divide.expressions().stream().noneMatch(e -> e instanceof MultiExpression)) {
      return divide;
    }
    var numerators = divide.expressions().subList(0, divide.expressions().size() - 1);
    var denominator = divide.expressions().get(divide.expressions().size() - 1);
    return divideBy(numerators, denominator);
  }

  private static ArithmeticExpression divideBy(List<ArithmeticExpression> numerators, ArithmeticExpression denominator) {
    for (int i = numerators.size() - 1; i >= 0; i--) {
      var numerator = numerators.get(i);
      var newDenominator = denominator;
      denominator = switch (numerator) {
        case Sum sum -> new Sum(sum.expressions().stream()
            .map(e -> divideBy(e, newDenominator))
            .toList());
        case Subtract subtract -> new Subtract(subtract.expressions().stream()
            .map(e -> divideBy(e, newDenominator))
            .toList());
        case Multiply multiply -> new Multiply(multiply.expressions().stream()
            .map(e -> divideBy(e, newDenominator))
            .toList());
        case Divide divide -> distribute(divide(Stream.concat(divide.expressions().stream(), Stream.of(newDenominator)).toList()));
        case ArithmeticValue value -> divideBy(value, denominator);
        case ValueOf valueOf -> divideBy(valueOf, denominator);
        case LengthOf lengthOf -> divideBy(lengthOf, denominator);
        case ByteLengthOf byteLengthOf -> divideBy(byteLengthOf, denominator);
        case ByteLengthOfBasicElement byteLengthOfBasicElement -> divideBy(byteLengthOfBasicElement, denominator);
        case SameExpression sameExpression -> divideBy(sameExpression, denominator);
        case GreaterThanExpression greaterThanExpression -> divideBy(greaterThanExpression, denominator);
        case GreaterThanOrEqualExpression greaterThanOrEqualExpression -> divideBy(greaterThanOrEqualExpression, denominator);
        case LessThanExpression lessThanExpression -> divideBy(lessThanExpression, denominator);
        case LessThanOrEqualExpression lessThanOrEqualExpression -> divideBy(lessThanOrEqualExpression, denominator);
        case NotSameExpression notSameExpression -> divideBy(notSameExpression, denominator);
        case ValueOfBit valueOfBit -> divideBy(valueOfBit, denominator);
      };
    }
    return denominator;
  }

  private static ArithmeticExpression divideBy(ArithmeticExpression numerator, ArithmeticExpression denominator) {
    if (numerator instanceof Divide n) {
      return divide(Stream.concat(n.expressions().stream(), Stream.of(denominator)).toList());
    } else if (numerator instanceof MultiExpression n && denominator instanceof MultiExpression) {
      return divideBy((List<ArithmeticExpression>) n.expressions(), (ArithmeticExpression) denominator);
    } else if (numerator instanceof ByteLengthOfBasicElement) {
      return divide(numerator, denominator);
    } else if (numerator instanceof LengthOf) {
      return divide(numerator, denominator);
    } else if (numerator instanceof ValueOf) {
      return divide(numerator, denominator);
    } else if (numerator instanceof Value) {
      if (denominator instanceof Value) {
        return divide(numerator, denominator);
      } else if (denominator instanceof Divide d) {
        return divide(Stream.concat(Stream.of(numerator), d.expressions().stream()).toList());
      }
    }
    throw new IllegalArgumentException("Cannot divide " + numerator + " by " + denominator);
  }

  static ArithmeticExpression simplify(Divide d) {
    var newExpressions = new ArrayList<ArithmeticExpression>();
    for (int i = 0; i < d.expressions().size(); i++) {
      var expression = d.expressions().get(i);
      if (expression instanceof ArithmeticValue v) {
        if (!(d.expressions().get(i + 1) instanceof Value)) {
          newExpressions.add(v);
          continue;
        }
        var value = v;
        for (int j = i + 1; j < d.expressions().size(); j++) {
          var other = d.expressions().get(j);
          if (other instanceof ArithmeticValue nv) {
            value = value.divide(nv);
            i = j;
          } else {
            i = j - 1;
            break;
          }
        }
        newExpressions.add(value);
      } else if (expression instanceof Sum sum) {
        newExpressions.add(Sum.simplify(sum));
      } else if (expression instanceof Subtract sub) {
        newExpressions.add(Subtract.simplify(sub));
      } else if (expression instanceof Multiply mul) {
        newExpressions.add(Multiply.simplify(mul));
      } else if (expression instanceof Divide div) {
        newExpressions.add(Divide.simplify(div));
      } else {
        newExpressions.add(expression);
      }
    }
    if (newExpressions.size() == 1) {
      return newExpressions.getFirst();
    }
    return new Divide(newExpressions);
  }

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner(" / ");
    for (Expression expression : expressions) {
      if (expression instanceof Sum || expression instanceof Subtract || expression instanceof Multiply || expression instanceof Divide) {
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

    Divide divide = (Divide) o;
    return expressions.equals(divide.expressions);
  }

  @Override
  public int hashCode() {
    return expressions.hashCode();
  }
}
