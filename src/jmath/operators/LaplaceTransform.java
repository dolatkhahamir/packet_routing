package jmath.operators;

import jmath.datatypes.Operator;
import jmath.datatypes.functions.Function2D;
import jmath.datatypes.functions.UnaryFunction;

public class LaplaceTransform implements Operator<UnaryFunction> {

    public static double laplaceValueAt(Function2D f, double x, double delta) {
        return Integral.infiniteU(t -> f.valueAt(t) * Math.exp(-x * t), 0, delta);
    }

    public static UnaryFunction laplaceOf(Function2D f, double delta) {
        return new UnaryFunction(x -> laplaceValueAt(f, x, delta));
    }

    @Override
    public UnaryFunction operate(Function2D f) {
        return null;
    }
}
