package jmath.functions.binaries;

import jmath.datatypes.functions.BinaryFunction;

@SuppressWarnings("unused")
public class Modulo extends BinaryFunction {
    private Modulo() {
        super((x, y) -> x % y);
    }

//    public static BinaryFunction f() {
//        return new Modulo();
//    }
}
