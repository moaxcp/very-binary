package com.github.moaxcp.verybinary;

public class Util {
  private Util() {
    throw new UnsupportedOperationException();
  }

  static byte[] mapIntsToBytes(int[] values) {
    var b = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      b[i] = (byte) values[i];
    }
    return b;
  }

  static short[] mapIntsToShorts(int[] values) {
    var s = new short[values.length];
    for(int i = 0; i < values.length; i++) {
      s[i] = (short) values[i];
    }
    return s;
  }
}
