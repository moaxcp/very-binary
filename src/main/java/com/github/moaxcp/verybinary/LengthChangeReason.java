package com.github.moaxcp.verybinary;

public enum LengthChangeReason {
  ALIGN,
  /**
   * Item requested to be removed from array
   */
  DEALLOCATED,
  /**
   * Item requested to be added to array
   */
  ALLOCATED,
  /**
   * Length field changed and array needs to adjust from previous size
   */
  RESIZED_BY_LENGTH_FIELD,

  RESIZED_BY_BYTE_LENGTH_FIELD
}
