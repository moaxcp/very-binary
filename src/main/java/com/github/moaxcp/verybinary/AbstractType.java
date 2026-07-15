package com.github.moaxcp.verybinary;


import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public sealed abstract class AbstractType<SELF extends AbstractType<SELF>> implements Type<SELF> permits PadType, ValueType {

  protected final int position;
  @Nullable
  protected ComplexType<?> parent;
  protected final List<ByteLengthListener> byteLengthListeners = new ArrayList<>();

  protected AbstractType(int position, @Nullable ComplexType<?> parent) {
    this.position = position;
    this.parent = parent;
  }

  @Override
  public final int getPosition() {
    return position;
  }

  @Override
  public final <V extends ComplexType<V>> @Nullable ComplexType<V> getParent() {
    return (ComplexType<V>) parent;
  }

  final List<ByteLengthListener> getByteLengthListeners() {
    return byteLengthListeners;
  }

  final SELF addByteLengthListeners(List<ByteLengthListener> listeners) {
    byteLengthListeners.addAll(listeners);
    return (SELF) this;
  }

  final SELF addByteLengthListener(ByteLengthListener listener) {
    byteLengthListeners.add(listener);
    return (SELF) this;
  }

  abstract SELF copy(int position, @Nullable ComplexType<?> parent);

  final SELF setParent(ComplexType<?> parent) {
    this.parent = parent;
    return (SELF) this;
  }

  @Override
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

  @Override
  public final void notifyByteLengthChange(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previousLength, long currentLength) {
    getByteLengthListeners().forEach(b -> b.byteLengthChanged(reason, pointer, previousLength, currentLength));
  }

  @Override
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
