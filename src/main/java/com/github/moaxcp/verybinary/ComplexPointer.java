package com.github.moaxcp.verybinary;

public sealed interface ComplexPointer<SELF extends ComplexPointer<SELF, TYPE>, TYPE extends ComplexType<TYPE>> extends Pointer<ComplexPointer<SELF, TYPE>, TYPE> permits Struct {
}
