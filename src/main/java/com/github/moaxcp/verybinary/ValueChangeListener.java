package com.github.moaxcp.verybinary;

import static com.github.moaxcp.verybinary.LengthChangeReason.RESIZED_BY_BYTE_LENGTH_FIELD;
import static com.github.moaxcp.verybinary.LengthChangeReason.RESIZED_BY_LENGTH_FIELD;
import static com.github.moaxcp.verybinary.ValueChangeListener.ValueChangeReason.SET_VALUE;

public interface ValueChangeListener {

  enum ValueChangeReason {
    /**
     * Array field was added too or removed from so length field needs to adjust
     */
    SET_BY_ARRAY_LENGTH,
    SET_BY_BYTE_LENGTH,
    /**
     * Value was set by request
     */
    SET_VALUE
  }

  static ExtendBytesListener extendBytesListener(int position) {
    return new ExtendBytesListener(position);
  }

  static ExtendArrayListener extendArrayListener(int position) {
    return new ExtendArrayListener(position);
  }

  class ExtendBytesListener implements ValueChangeListener {
    private final int position;

    public ExtendBytesListener(int position) {
      this.position = position;
    }

    @Override
    public void valueChanged(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Object oldValue, Object newValue) {
      if (reason != SET_VALUE) {
        return;
      }
      var current = ((Number) newValue).longValue();
      var old = ((Number) oldValue).longValue();
      ValueType<?, ?> type = pointer.getType(position);
      if (current > old) {
        var length = current - old;
        type.allocate(RESIZED_BY_BYTE_LENGTH_FIELD, pointer, old, length);
      } else {
        var length = old - current;
        type.remove(RESIZED_BY_BYTE_LENGTH_FIELD, pointer, old - 1, length);
      }
    }

    @Override
    public String toString() {
      return "ExtendBytesListener{" +
          "position=" + position +
          '}';
    }
  }

  class ExtendArrayListener implements ValueChangeListener {
    private final int position;

    public ExtendArrayListener(int position) {
      this.position = position;
    }

    @Override
    public void valueChanged(ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Object oldValue, Object newValue) {
      if (reason != SET_VALUE) {
        return;
      }
      var current = ((Number) newValue).longValue();
      var old = ((Number) oldValue).longValue();
      ValueType<?, ?> type = pointer.getType(position);
      if (current > old) {
        var length = current - old;
        type.allocate(RESIZED_BY_LENGTH_FIELD, pointer, old, length);
      } else {
        var length = old - current;
        type.remove(RESIZED_BY_LENGTH_FIELD, pointer, old - 1, length);
      }
    }

    @Override
    public String toString() {
      return "ExtendArrayListener{" +
          "position=" + position +
          '}';
    }
  }

  void valueChanged(ValueChangeListener.ValueChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long index, Object oldValue, Object newValue);
}
