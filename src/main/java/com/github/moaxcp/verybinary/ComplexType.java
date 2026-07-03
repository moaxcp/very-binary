package com.github.moaxcp.verybinary;

public sealed interface ComplexType permits StructType {

  <V extends Type<?>> V getType(int position);
}
