package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.Struct;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.verybinary.Builders.struct;
import static com.github.moaxcp.verybinary.Builders.structType;
import static com.github.moaxcp.verybinary.math.Expression.constant;
import static org.assertj.core.api.Assertions.assertThat;

class Float32ListTest {
  @Test
  void testGetFloat32() {
    Struct struct = struct(structType().float32Array(constant(2)).build())
        .build()
        .setFloat32(0, 1.1f, 2.2f);

    Float32List list = struct.getFloat32Array(0);
    assertThat(list.getFloat32(0)).isEqualTo(1.1f);
    assertThat(list.getFloat32(1)).isEqualTo(2.2f);
  }

  @Test
  void testSetAndGet() {
    Struct struct = struct(structType().float32Array(constant(2)).build())
        .build();

    Float32List list = struct.getFloat32Array(0);
    list.set(0, 3.3f);
    list.set(1, 4.4f);

    assertThat(list.get(0)).isEqualTo(3.3f);
    assertThat(list.get(1)).isEqualTo(4.4f);
  }

  @Test
  void testIteration() {
    Struct struct = struct(structType().float32Array(constant(3)).build())
        .build()
        .setFloat32(0, 1.0f, 2.0f, 3.0f);

    Float32List list = struct.getFloat32Array(0);
    var iter = list.iterator();
    assertThat(iter.hasNext()).isTrue();
    assertThat(iter.nextFloat()).isEqualTo(1.0f);
    assertThat(iter.nextFloat()).isEqualTo(2.0f);
    assertThat(iter.nextFloat()).isEqualTo(3.0f);
    assertThat(iter.hasNext()).isFalse();
  }

  @Test
  void testToFloat32List() {
    float[] values = {1.5f, 2.5f};
    Float32List list = Float32List.toFloat32List(values);
    assertThat(list.size64()).isEqualTo(2);
    assertThat(list.getFloat32(0)).isEqualTo(1.5f);
    assertThat(list.getFloat32(1)).isEqualTo(2.5f);
  }

  @Test
  void testEqualsAndHashCode() {
    Float32List list1 = Float32List.toFloat32List(new float[]{1.0f, 2.0f});
    Float32List list2 = Float32List.toFloat32List(new float[]{1.0f, 2.0f});
    Float32List list3 = Float32List.toFloat32List(new float[]{1.0f, 3.0f});

    assertThat(list1).isEqualTo(list2);
    assertThat(list1).isNotEqualTo(list3);
    assertThat(list1.hashCode()).isEqualTo(list2.hashCode());
  }

  @Test
  void testCopy() {
    Float32List list1 = Float32List.toFloat32List(new float[]{1.0f, 2.0f});
    Float32List list2 = list1.copy();

    assertThat(list1).isEqualTo(list2);
    assertThat(list1).isNotSameAs(list2);
  }
}
