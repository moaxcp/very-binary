# very-binary

An implementation of C structs for Java. A struct directly manipulates a byte array formatting binary data like a C struct.

## Memory

Memory is an interface to the memory that stores the data. It provides low level access to encoding and decoding
basicTypeInfo types. All memory is addressed with 64-bit longs. There are fluent builder methods for creating packed memory
with values. Memory can be backed by a byte array, ByteBuffer, memory mapped file, or memory segment. Memory needs to
support listeners to know when memory is allocated or freed. This allows pointers (struct, union, etc) to adjust their
offset when it changes.

## Types

Hierarchy

Type
* PadType
* AlignType
* ValueType
  * ComplexType
    * StructType
    * UnionType
  * PrimitiveType
    * BoolType
    * NumberType
      * Int8Type
      * Uint8Type
      * Int16Type
      * Uint16Type
      * Int32Type
      * Uint32Type
      * Int64Type
      * Uint64Type
      * Float32Type
      * Float64Type
  * ListType
    * ComplexListType
      * StructListType
      * UnionListType
    * PrimitiveListType
      * BoolListType
      * Int8ListType
      * Uint8ListType
      * Int16ListType
      * Uint16ListType
      * Int32ListType
      * Uint32ListType
      * Int64ListType
      * Uint64ListType
      * Float32ListType
      * Float64ListType

Types define the memory layout of the data. Pointers and types a tightly coupled. The type knows how to read and write
data to the memory but not where. The pointer is used to calculate the offset in memory where operations are performed.
Things like allocation, removal, and setting values are performed by the type using a pointer. This is why most methods
have a pointer parameter.

### Supported Data Types

| Type          | Java Type             | Range                                                   | Byte Width           |
|:--------------|:----------------------|:--------------------------------------------------------|:---------------------|
| `bool`        | `boolean`             | `true`, `false`                                         | 1                    |
| `boolList`    | `BooleanBigList`      | `true`, `false`                                         | up to Long.MAX_VALUE |
| `int8`        | `byte`                | -128 to 127                                             | 1                    |
| `int8List`    | `ByteBigList`         | -128 to 127                                             | up to Long.MAX_VALUE |
| `uint8`       | `short`               | 0 to 255                                                | 1                    |
| `uint8List`   | `ShortBigList`        | 0 to 255                                                | up to Long.MAX_VALUE |
| `int16`       | `short`               | -32,768 to 32,767                                       | 2                    |
| `int16List`   | `ShortBigList`        | -32,768 to 32,767                                       | up to Long.MAX_VALUE |
| `uint16`      | `int`                 | 0 to 65,535                                             | 2                    |
| `uint16List`  | `IntBigList`          | 0 to 65,535                                             | up to Long.MAX_VALUE |
| `int32`       | `int`                 | -2,147,483,648 to 2,147,483,647                         | 4                    |
| `int32List`   | `IntBigList`          | -2,147,483,648 to 2,147,483,647                         | up to Long.MAX_VALUE |
| `uint32`      | `long`                | 0 to 4,294,967,295                                      | 4                    |
| `uint32List`  | `LongBigList`         | 0 to 4,294,967,295                                      | up to Long.MAX_VALUE |
| `int64`       | `long`                | -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 | 8                    |
| `int64List`   | `LongBigList`         | -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 | up to Long.MAX_VALUE |
| `uint64`      | `BigInteger`          | 0 to 18,446,744,073,709,551,615                         | 8                    |
| `uint64List`  | `BigList<BigInteger>` | 0 to 18,446,744,073,709,551,615                         | up to Long.MAX_VALUE |
| `float32`     | `float`               | IEEE 754 single precision                               | 4                    |
| `float32List` | `FloatBigList`        | IEEE 754 single precision                               | up to Long.MAX_VALUE |
| `float64`     | `double`              | IEEE 754 double precision                               | 8                    |
| `float64List` | `DoubleBigList`       | IEEE 754 double precision                               | up to Long.MAX_VALUE |
| `struct`      | `Struct`              | Any struct                                              | up to Long.MAX_VALUE |
| `structList`  | `BigList<Struct>`     | Any struct                                              | up to Long.MAX_VALUE |
| `union`       | `Union`               | Any union                                               | up to Long.MAX_VALUE |
| `unionList`   | `BigList<Union>`      | Any union                                               | up to Long.MAX_VALUE |

### Features

* ByteLengthListener
* LengthListener
* ValueListener
* IndexedValueListener
* ListValueListener
* LengthExpression
* ConstantValue
* DefaultValue

These features are used to

Lock field to specific value.
Set default value when a field is allocated.
Grow lists when a length field is set.
Set a length field when a list item is added.
Allocated and remove a field when a bit is toggled.


## Pointers

### Struct


### Union



Features include

* data types: int8, uint8, int16, uint16, int32, uint32, int64, uint64, float32, float64, structs, and arrays for each type.
* Uses primitives instead of wrapper classes when possible.
* Position-based lookups instead of strings (maybe become optional in the future).
* A schema definition that can be reused (sharing type definitions saves memory)
* A fluent api for building schemas and structs.

A struct is an object that combines a schema of fields and memory (byte array). It provides a number of convenient 
methods for reading and writing data in memory based on the schema. Similar to a pointer a struct can be changed to point
to different memory for reuse.

Memory can be Little Endian or Big Endian. Currently, it is limited to the max size of a byte array.

This project is built to support the protocol in x11-client.

Current task

Remove byteLengthExpression from lists.
lengthExpression is required.
if the type is constant then lengthExpression should be set to constant of the constant value length.

todo:

Add support for default values. Constant values might actually be useless.
* Add verification for being allocated
    * When a struct is created and allocated is true the byte array should be checked against the array for valid values.
    * Each type should be able to do this.
* getters on all types returning an array or List should return an empty value if the length is 0.
* allocation length needs to also check byteLengthExpression for a length value.
* move constant value down to each type to prevent wrapping.
* arrays should have constant values that are an array instead of a single value.
* add verification for struct constant matching type.
* There no implementation for large ByteArrays. Even with an implementation much of the design for arrays will not.
  * arrays would need a collection type that can be larger than a java array.
* Add tests for 0 length getters. They should return an empty array or List.
* fix isConstant/isConstantValue/isFixedLength with array/list types.
  * Constants are now a list or array instead of a single repeated value. This means a length expression is not required because the constant value defines the length.
  * if and indexed type is constant then it shouldn't even have a length expression.
  * fixed length should check if there is a constant
* Struct and StructList types should not have constants instead each member should be constant. isConstant should check each field
* isFixedLength does not need a pointer. It needs the parent type which is used in pad and length expression.
* Paths to navigate to sub fields. Could be like xpath or json paths.
* There are two concepts for length. Array length and byte length. Maybe the listeners can be handled the same way. And if the value doesn't fit into a byte length (long list with length of 6) an exception should be thrown.