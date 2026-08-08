package com.github.moaxcp.verybinary.uint64;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.BasicTypeInfo.UINT64;
import static com.github.moaxcp.verybinary.list.Uint64List.toUint64List;
import static com.github.moaxcp.verybinary.math.Int8Value.int8Value;
import static org.assertj.core.api.Assertions.assertThat;

public class Uint64TypeTest {

  @Test
  void copy() {
    var type = structType()
        .uint64()
        .basic().constant(toUint64List(List.of(BigInteger.valueOf(5)))).uint64()
        .align(2)
        .build();
    var copy = type.copy(15, null);
    assertThat(copy.getPosition()).isEqualTo(15);
  }

  @Test
  void getByteLength() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getByteLength()).isEqualTo(UINT64.size());
    assertThat(struct.getType(0).getByteLength(struct)).isEqualTo(UINT64.size());
  }

  @Test
  void isFixedByteLength() {
    var struct = struct()
        .uint64()
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }

  @Test
  void isFixedByteLengthArray() {
    var struct = struct()
        .uint64List(int8Value(5))
        .build();

    assertThat(struct.getType(0).isFixedByteLength()).isTrue();
    assertThat(struct.isFixedLength()).isTrue();
  }
}
