package com.github.moaxcp.verybinary;


import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class Type<SELF extends Type<SELF>> permits PadType, StructListType, Uint64ListType, ValueType {

  protected final int position;
  @Nullable
  protected final ComplexType parent;
  protected final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();

  protected Type(int position, @Nullable ComplexType parent) {
    this.position = position;
    this.parent = parent;
  }

  public final int getPosition() {
    return position;
  }

  @Nullable
  public final ComplexType getParent() {
    return parent;
  }

  public final List<ByteLengthListener> getByteLengthListeners() {
    return byteLengthListeners;
  }

  public final SELF addByteLengthListeners(List<ByteLengthListener> listeners) {
    byteLengthListeners.addAll(listeners);
    return (SELF) this;
  }

  public abstract SELF copy(int position, @Nullable ComplexType parent);

  /**
   * true if the bytes for this type are always constant.
   * @return
   */
  public abstract boolean isConstant();

  public final long getOffset(Pointer<?, ? extends Type<?>> pointer) {
    return switch (pointer) {
      case Struct struct -> {
        long offset = pointer.getOffset();
        for (int i = 0; i < getPosition(); i++) {
          offset += struct.getType(i).getByteLength(pointer);
        }
        yield offset;
      }
    };
  }

  protected abstract long getAllocationLength();

  public abstract long getByteLength(Pointer<?, ? extends Type<?>> pointer);

  /**
   * Returns true of the byte length of this type is constant.
   * @return
   */
  public abstract boolean isFixedLength();

  public abstract void allocate(Pointer<?, ? extends Type<?>> pointer);

  public final void notifyByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previousLength, long currentLength) {
    getByteLengthListeners().forEach(b -> b.byteLengthChanged(reason, pointer, previousLength, currentLength));
  }

  public final void callWithByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, Runnable runnable) {
    if (getByteLengthListeners().isEmpty()) {
      runnable.run();
      return;
    }
    var previous = getByteLength(pointer);
    runnable.run();
    var current = getByteLength(pointer);
    notifyByteLengthChange(reason, pointer, previous, current);
  }
}
