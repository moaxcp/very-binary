package com.github.moaxcp.verybinary;

@FunctionalInterface
public interface LengthListener {

  static LengthListener lengthField(int position) {
    return new SetLengthFieldListener(position);
  }

  class SetLengthFieldListener implements LengthListener {
    private final int position;

    public SetLengthFieldListener(int position) {
      this.position = position;
    }

    @Override
    public void arrayLengthChanged(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previous, long current) {
      if(reason == LengthChangeReason.RESIZED_BY_LENGTH_FIELD) {
        return;
      }
      switch (pointer) {
        case Struct struct:
          ((LengthType) struct.getType(position)).setForArrayLength(pointer, current);
      }
    }

    @Override
    public String toString() {
      return "SetLengthFieldChangeListener{" +
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

  void arrayLengthChanged(LengthChangeReason reason, Pointer<?, ? extends Type<?>> pointer, long previous, long current);
}
