# very-binary

An implementation of C structs for Java. A struct directly manipulates a byte array formatting binary data like a C struct.

## Supported Data Types

| Type | Java Type | Range | Byte Width |
| :--- | :--- | :--- | :--- |
| `bool` | `boolean` | `true`, `false` | 1 |
| `int8` | `byte` | -128 to 127 | 1 |
| `uint8` | `short` | 0 to 255 | 1 |
| `int16` | `short` | -32,768 to 32,767 | 2 |
| `uint16` | `int` | 0 to 65,535 | 2 |
| `int32` | `int` | -2,147,483,648 to 2,147,483,647 | 4 |
| `uint32` | `long` | 0 to 4,294,967,295 | 4 |
| `int64` | `long` | -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807 | 8 |
| `uint64` | `BigInteger` | 0 to 18,446,744,073,709,551,615 | 8 |
| `float32` | `float` | IEEE 754 single precision | 4 |
| `float64` | `double` | IEEE 754 double precision | 8 |

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



todo:

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