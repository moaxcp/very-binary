package com.github.moaxcp.verybinary;

public record ShiftBytes(long offset, long size) {
  public static ShiftBytes shiftBytes(long offset, long size) {
    return new ShiftBytes(offset, size);
  }
}
