package com.github.moaxcp.verybinary;

@FunctionalInterface
public interface ByteLengthListener {

  static ByteLengthListener lengthField(int position) {
    return new ByteLengthListener.SetLengthFieldListener(position);
  }

  static ByteLengthListener align(int alignPosition) {
    return new AlignByteListener(alignPosition);
  }

  class AlignByteListener implements ByteLengthListener {
    private final int alignPosition;

    public AlignByteListener(int alignPosition) {
      this.alignPosition = alignPosition;
    }

    @Override
    public void byteLengthChanged(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previous, long current) {
      if (reason != LengthChangeReason.ALIGN) {
        return;
      }
      ((PadType) pointer.getType(alignPosition)).reAlign(pointer, previous, current);
    }

    @Override
    public String toString() {
      return "AlignByteChangeListener{" +
          "alignPosition=" + alignPosition +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      AlignByteListener that = (AlignByteListener) o;
      return alignPosition == that.alignPosition;
    }

    @Override
    public int hashCode() {
      return alignPosition;
    }
  }

  class SetLengthFieldListener implements ByteLengthListener {
    private final int position;

    public SetLengthFieldListener(int position) {
      this.position = position;
    }

    @Override
    public void byteLengthChanged(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previous, long current) {
      if(reason == LengthChangeReason.RESIZED_BY_BYTE_LENGTH_FIELD) {
        return;
      }
      ((NumberType) pointer.getType(position)).setForByteLength(pointer, current);
    }

    @Override
    public String toString() {
      return "SetLengthFieldListener{" +
          "position=" + position +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      SetLengthFieldListener that = (SetLengthFieldListener) o;
      return position == that.position;
    }

    @Override
    public int hashCode() {
      return position;
    }
  }

  void byteLengthChanged(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previous, long current);
}
