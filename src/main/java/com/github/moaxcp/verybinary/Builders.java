package com.github.moaxcp.verybinary;

public class Builders {

  private Builders() {
  }

  public static StructBuilder struct() {
    return new StructBuilder();
  }
}
