package com.github.moaxcp.verybinary;

public sealed abstract class ComplexType<SELF extends ComplexType<SELF>> extends Type<SELF> permits StructType {
  public ComplexType(int position) {
    super(position);
  }
}
