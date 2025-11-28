# High-Level Requirements

* A Struct should read and write data in a ByteArray using a StructType.
* StructType is the schema for the data format of the Struct.
* Structs can share the same StructType.
* Struct members are primarily position based.

# Layers of abstraction

## byte[]

The first layer is the java byte[]. It contains the primitive data (int8, uint8, int16, etc.) of a Struct formatted by 
the StructType.

## primitive types

These include boolean, integers, and floats with specific bit depths. Integers can be signed or unsigned. Signed integers use 
twos-complement encoding.

* bool - 8 bit 0 (false) or >=1 (true) value
* int8 - 8 bit signed integer
* uint8 - 8 bit unsigned integer
* int16 - 16 bit signed integer
* uint16 - 16 bit unsigned integer
* int32 - 32 bit signed integer
* uint32 - 32 bit unsigned integer
* int64 - 64 bit signed integer
* uint32 - 64 bit unsigned integer
* float32 - 32 bit floating point number
* float64 - 64 bit floating point number

### NaN handling policy

For float32 and float64, NaN payloads and sign bits are preserved on round-trips. Serializers must use raw bit conversions
when writing floating-point values to avoid canonicalizing NaNs. Specifically: Float.floatToRawIntBits and
Double.doubleToRawLongBits are used during writes; reads use the corresponding intBitsToFloat and longBitsToDouble. This
ensures bit pattern preservation for all floating-point values, including NaNs.

# Serializer

Serializers implement how each primitive type is read and written to the byte[]. Serializers include Big Endian and 
Little Endian.

# ByteArray

The array is wrapped by a ByteArray class. It is only ever read and written to using a serializer in the ByteArray.
ByteArray should be indexed with a long so it can support larger memory (multiple byte arrays) in the future.
Each data type in ByteArray requires a set of public methods for example:

* getBool(long index) - get value at index
* boolean[] getBool(long index, long length) - get array at index
* List<Boolean> getBoolList(long index, long length) - get List at index
* ByteArray setBool(long index, boolean value) - set value at index
* ByteArray setBool(long index, boolean... values)- set array at index
* ByteArray setBool(long index, List<Boolean> values) set List at index
* ByteArray bool(boolean value) - allocate space and add value to end of bytes
* ByteArray bool(bool... value) - allocate space and add array of values to end of bytes
* ByteArray bool(List<Boolean> values) - allocate space and add List of values to end of bytes
* ByteArray addBool(long index, boolean value) - allocate space and add value at index of bytes
* ByteArray addBool(long index, boolean... values) - allocate space and add array at index of bytes
* ByteArray addBool(long index, List<Boolean> values) - allocate space and add List at index of bytes
* ByteArray removeBool(long index) - removes bool (1 byte) from bytes at index
* ByteArray removeBool(long index, long length) - removes bool array of length from bytes at index

A ByteArray can be preallocated to avoid a lot of byte shifts. As bytes are added the allocated variable is incremented.

ByteArray has listeners that can be added. Listeners are notified when bytes shift in the array. Bytes shift when add/remove
methods are called.

# Pointer

Pointer is an interface that brings together the offset in memory, ByteArray, and Type.

# Types

Types are an abstraction above the primitive types. Each primitive has a Type class and StructType is for structs.
Type classes define methods for manipulating the ByteArray. The offset of the pointer and the index of the field within
the pointer determines the index to change in the ByteArray.

# Struct

Struct is a pointer with StructType and ByteArray.

# StructType

Defines an ordered list of fields. Each field has an index and a Type.

