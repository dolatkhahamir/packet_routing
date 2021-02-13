package jmath.datatypes.functions;

import jmath.datatypes.tuples.Point2D;
import jmath.functions.utils.Sampling;

import java.util.List;

@SuppressWarnings("unused")
public interface Arc2D extends Function<Point2D, Double> {
    Arc2D NaN = t -> new Point2D(Double.NaN, Double.NaN);

    Point2D valueAt(double t);

    default List<Point2D> sample(double l, double u, double delta, int numOfThreads) {
        return Sampling.multiThreadSampling(this, l, u, delta, numOfThreads);
    }

    default UnaryFunction asFunction(double l, double u, double delta, int numOfThreads) {
        return Sampling.sampleToFunction(sample(l, u, delta, numOfThreads));
    }

    default UnaryFunction fx() {
        return new UnaryFunction(x -> valueAt(x).x);
    }

    default UnaryFunction fy() {
        return new UnaryFunction(x -> valueAt(x).y);
    }

    default Arc2D derivative(int order, double delta) {
        return t -> new Point2D(fx().derivative(delta, order).valueAt(t), fy().derivative(delta, order).valueAt(t));
    }

    @Override
    default Point2D valueAt(Double t) {
        return valueAt(t.doubleValue());
    }

    @Override
    default Point2D atOrigin() {
        return valueAt(0);
    }

}
