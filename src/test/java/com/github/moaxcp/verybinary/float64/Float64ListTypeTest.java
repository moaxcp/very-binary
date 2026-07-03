package com.github.moaxcp.verybinary.float64;

import com.github.moaxcp.verybinary.ByteArray;
import com.github.moaxcp.verybinary.Struct;
import com.github.moaxcp.verybinary.list.Float64List;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;

public class Float64ListTypeTest {
  @Test
  void testGetSet() {
    Struct struct = struct(structType()
        .float64Array(constant(2))
        .build())
        .build();

    struct.setFloat64(0, 0, 1.23);
    struct.setFloat64(0, 1, 4.56);

    assertThat(struct.getFloat64(0, 0)).isEqualTo(1.23);
    assertThat(struct.getFloat64(0, 1)).isEqualTo(4.56);
  }

  @Test
  void testGetArray() {
    Struct struct = struct(structType()
        .float64Array(constant(2))
        .build())
        .build();

    struct.setFloat64(0, new double[]{1.1, 2.2});

    assertThat(struct.getFloat64RawArray(0)).containsExactly(1.1, 2.2);
  }

  @Test
  void testGetList() {
    Struct struct = struct(structType()
        .float64Array(constant(2))
        .build())
        .build();

    struct.setFloat64(0, List.of(3.3, 4.4));

    assertThat(struct.getFloat64List(0)).containsExactly(3.3, 4.4);
  }

  @Test
  void testAdd() {
    Struct struct = struct(structType()
        .float64Array(constant(0))
        .build())
        .build();

    struct.addFloat64(0, 5.5);
    struct.addFloat64(0, 6.6);

    assertThat(struct.getFloat64RawArray(0)).containsExactly(5.5, 6.6);
  }

  @Test
  void testFloat64List() {
    Struct struct = struct(structType()
        .float64Array(constant(2))
        .build())
        .build();

    Float64List list = struct.getFloat64Array(0);
    list.set(0, 7.7);
    list.set(1, 8.8);

    assertThat(list.getFloat64(0)).isEqualTo(7.7);
    assertThat(list.getFloat64(1)).isEqualTo(8.8);
    assertThat(list).containsExactly(7.7, 8.8);
  }
}
