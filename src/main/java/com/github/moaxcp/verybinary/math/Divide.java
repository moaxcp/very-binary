package com.github.moaxcp.verybinary.math;

import com.github.moaxcp.verybinary.ComplexType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public final class Divide implements MultiExpression {

  private final List<Expression> expressions;

  Divide(Expression... expressions) {
    if (expressions == null || expressions.length < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.of(expressions);
  }

  public Divide(List<? extends Expression> expressions) {
    if (expressions == null || expressions.size() < 2) {
      throw new IllegalArgumentException("expressions must have at least two elements");
    }
    this.expressions = List.copyOf(expressions);
  }

  public static Divide divide(Expression... expressions) {
    return new Divide(expressions);
  }

  public static Divide divide(List<? extends Expression> expressions) {
    return new Divide(expressions);
  }

  public List<Expression> expressions() {
    return expressions;
  }

  @Override
  public boolean isConstant(ComplexType<?> parent) {
    return expressions.stream().allMatch(e -> e.isConstant(parent));
  }

  @Override
  public long constantValue(ComplexType<?> parent) {
    long result = expressions.get(0).constantValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result /= expressions.get(i).constantValue(parent);
    }
    return result;
  }

  @Override
  public long defaultValue(ComplexType<?> parent) {
    long result = expressions.get(0).defaultValue(parent);
    for (int i = 1; i < expressions.size(); i++) {
      result /= expressions.get(i).defaultValue(parent);
    }
    return result;
  }

  @Override
  public long evaluate(Pointer<?, ? extends Type<?>> pointer) {
    long result = expressions.get(0).evaluate(pointer);
    for (int i = 1; i < expressions.size(); i++) {
      result /= expressions.get(i).evaluate(pointer);
    }
    return result;
  }

  public static Expression distribute(Divide divide) {
    if (divide.expressions().stream().noneMatch(e -> e instanceof MultiExpression)) {
      return divide;
    }
    var numerators = divide.expressions().subList(0, divide.expressions().size() - 1);
    var denominator = divide.expressions().get(divide.expressions().size() - 1);
    return divideBy(numerators, denominator);
  }

  private static Expression divideBy(List<Expression> numerators, Expression denominator) {
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
        case Constant constant -> divideBy(constant, denominator);
        case Variable variable -> divideBy(variable, denominator);
        case LengthOf lengthOf -> divideBy(lengthOf, denominator);
        case ByteLengthOf byteLengthOf -> divideBy(byteLengthOf, denominator);
        case ByteLengthOfBasicElement byteLengthOfBasicElement -> divideBy(byteLengthOfBasicElement, denominator);
      };
    }
    return denominator;
  }

  private static Expression divideBy(Expression numerator, Expression denominator) {
    if (numerator instanceof Divide n) {
      return divide(Stream.concat(n.expressions().stream(), Stream.of(denominator)).toList());
    } else if (numerator instanceof MultiExpression n && denominator instanceof MultiExpression d) {
      return divideBy(n.expressions(), d);
    } else if (numerator instanceof ByteLengthOfBasicElement) {
      return divide(numerator, denominator);
    } else if (numerator instanceof LengthOf) {
      return divide(numerator, denominator);
    } else if (numerator instanceof Variable) {
      return divide(numerator, denominator);
    } else if (numerator instanceof Constant) {
      if (denominator instanceof Constant) {
        return divide(numerator, denominator);
      } else if (denominator instanceof Divide d) {
        return divide(Stream.concat(Stream.of(numerator), d.expressions().stream()).toList());
      }
    }
    throw new IllegalArgumentException("Cannot divide " + numerator + " by " + denominator);
  }

  static Expression simplify(Divide d) {
    var newExpressions = new ArrayList<Expression>();
    for (int i = 0; i < d.expressions().size(); i++) {
      var expression = d.expressions().get(i);
      if (expression instanceof Constant c) {
        if (!(d.expressions().get(i + 1) instanceof Constant)) {
          newExpressions.add(c);
          continue;
        }
        var value = c.value();
        for (int j = i + 1; j < d.expressions().size(); j++) {
          var other = d.expressions().get(j);
          if (other instanceof Constant nc) {
            value /= nc.value();
            i = j;
          } else {
            i = j - 1;
            break;
          }
        }
        newExpressions.add(new Constant(value));
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
