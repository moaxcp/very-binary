package com.github.moaxcp.verybinary;

public class StructBuilder extends StructTypeBuilder<StructBuilder> {

  private boolean allocated = false;
  private long offset = 0;
  private ByteArray bytes = new ByteArray();
  
  public StructBuilder allocated() {
    allocated = true;
    return this;
  }

  public StructBuilder offset(long offset) {
    this.offset = offset;
    return this;
  }

  public StructBuilder fromBytes(ByteArray memory) {
    this.bytes = memory;
    this.allocated = true;
    return this;
  }

  public StructBuilder fromBytes(byte[] bytes) {
    this.bytes = new ByteArray(bytes);
    this.allocated = true;
    return this;
  }

  public Struct build() {
    return new Struct(allocated, -1, offset, super.toStructType(), bytes);
  }
}
