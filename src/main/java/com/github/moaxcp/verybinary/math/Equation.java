package com.github.moaxcp.verybinary.math;

import org.jspecify.annotations.Nullable;

public class Equation {
  private Expression left;
  private Expression right;

  public Equation(Expression left, Expression right) {
    this.left = left;
    this.right = right;
  }

  public Expression getLeft() {
    return left;
  }

  public Expression getRight() {
    return right;
  }

  public @Nullable Equation isolateVariable(int position) {
    var leftVariables = left.findVariables(position);
    var rightVariables = right.findVariables(position);

    return null;
  }
}
