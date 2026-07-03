package com.github.moaxcp.verybinary;

public class Builders {

  private Builders() {
  }

  public static StructBuilder struct() {
    return new StructBuilder();
  }

  public static StructBuilder struct(StructType type) {
    return struct().from(type);
  }

  public static StructTypeStructTypeBuilder structType() {
    return new StructTypeStructTypeBuilder();
  }

  public static StructListTypeStructTypeBuilder structListType() {
    return new StructListTypeStructTypeBuilder();
  }

  public static StructTypeStructTypeBuilder structType(StructType type) {
    return new StructTypeStructTypeBuilder().from(type);
  }
}
