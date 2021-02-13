package jmath.operators;

import jmath.datatypes.Operator;
import jmath.datatypes.functions.BinaryFunction;
import jmath.datatypes.functions.Function2D;
import jmath.datatypes.functions.Function3D;
import jmath.datatypes.functions.UnaryFunction;
import jmath.datatypes.tuples.Point2D;
import jmath.functions.utils.Sampling;

import java.util.List;

@SuppressWarnings("unused")
public final class Derivative implements Operator<UnaryFunction> {
    private final double stepLen;

    private Derivative(double stepLen) {
        this.stepLen = stepLen;
    }

    @Override
    public UnaryFunction operate(Function2D f) {
        return new UnaryFunction(x -> (f.valueAt(x + stepLen) - f.valueAt(x)) / (stepLen * 2) -
                (f.valueAt(x - stepLen) - f.valueAt(x)) / (stepLen * 2));
    }

    public static UnaryFunction derivative(Function2D f, int n, double delta) {
        if (n <= 0)
            return new UnaryFunction(f);
        Derivative d = new Derivative(delta);
        UnaryFunction res = new UnaryFunction(f);
        for (int i = 0; i < n; i++)
            res = d.operate(res);
        return res;
    }

    public static UnaryFunction derivative(Function2D f, double delta) {
        return derivative(f, 1, delta);
    }

    public static BinaryFunction partialX(Function3D f, double delta) {
        return new BinaryFunction((x, y) -> derivative(new BinaryFunction(f).makeYFix(y), delta).valueAt(x));
    }

    public static BinaryFunction partialY(Function3D f, double delta) {
        return new BinaryFunction((x, y) -> derivative(new BinaryFunction(f).makeXFix(x), delta).valueAt(x));
    }

    public static UnaryFunction derivative(List<Point2D> sample, double delta) {
        return Sampling.sampleToFunction(sample).derivative(delta);
    }

    public static UnaryFunction derivative(List<Point2D> sample, int n, double delta) {
        return Sampling.sampleToFunction(sample).derivative(delta, n);
    }
}
