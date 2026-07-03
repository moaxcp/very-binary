package com.github.moaxcp.verybinary.list;

import com.github.moaxcp.verybinary.ByteArray;
import com.github.moaxcp.verybinary.ListType;
import com.github.moaxcp.verybinary.Pointer;
import com.github.moaxcp.verybinary.Type;

public abstract class BinaryList<SELF extends BinaryList<SELF, T>, T> implements Iterable<T> {

  protected final Pointer<?,? extends Type<?>> pointer;
  protected final ListType<?, T, ? extends BinaryList<SELF, T>> type;
  protected final long indexOffset;
  protected final long length;
  protected final boolean checkLength;

  protected BinaryList(Pointer<?, ? extends Type<?>> pointer, ListType<?, T, SELF> type, long indexOffset, long length, boolean checkLength) {
    this.type = type;
    this.pointer = pointer;
    this.indexOffset = indexOffset;
    this.length = length;
    this.checkLength = checkLength;
  }

  protected long getIndex(long index) {
    if(checkLength && index >= length) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for length " + length);
    }
    return index + indexOffset;
  }

  public T get(long index) {
    return type.get(pointer, getIndex(index));
  }

  public void set(long index, T value) {
    type.set(pointer, getIndex(index), value);
  }

  public long size64() {
    return type.getLength(pointer);
  }

  /**
   * replaces the bytes in bytes with the bytes of this list at index and length. If the length is greater than or
   * shorter than the length of this list bytes are added or removed as needed to replace the bytes.
   * @param destination
   * @param destinationIndex
   * @param destinationLength
   */
  public void replaceBytes(ByteArray destination, long destinationIndex, long destinationLength) {
    destination.replace(destinationIndex, destinationLength, pointer.getByteArray(), type.getOffset(pointer), type.getByteLength(pointer));
  }

  public abstract SELF copy();

  public long getByteLength() {
    return type.getByteLength(pointer);
  }
}
