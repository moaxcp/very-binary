package com.github.moaxcp.verybinary;

import java.util.List;

public sealed interface ComplexType<SELF extends ComplexType<SELF>> extends Type<SELF> permits StructType {



  List<Type<?>> getTypes();
  <V extends Type<?>> V getType(int position);
}
