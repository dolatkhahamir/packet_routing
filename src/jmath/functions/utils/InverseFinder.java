package jmath.functions.utils;

import jmath.datatypes.functions.Function2D;
import jmath.datatypes.functions.UnaryFunction;
import jmath.datatypes.tuples.Point2D;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class InverseFinder {
    public static final double DEFAULT_TOLERANCE = 0.00001;

    public static UnaryFunction bySampling(Function2D f, double l, double u, double delta, double tolerance) {
        return bySampling(Sampling.sample(f, l, u, delta), tolerance);
    }

    public static UnaryFunction bySampling(List<Point2D> sample, double tolerance) {
        return new UnaryFunction(x -> {
            sample.forEach(e -> e.set(e.x, Math.abs(e.y - x)));
            Point2D.setComparatorMode(Point2D.Y_COMPARE);
            Collections.sort(sample);
            sample.removeIf(e -> e.y > tolerance);
            if (sample.size() == 0)
                return Double.NaN;
            return sample.get(0).x;
        });
    }

    public static UnaryFunction bySampling(Function2D f, double l, double u, double delta) {
        return bySampling(f, l, u, delta, DEFAULT_TOLERANCE);
    }

    public static UnaryFunction bySampling(List<Point2D> sample) {
        return bySampling(sample, DEFAULT_TOLERANCE);
    }

    public static UnaryFunction byReSampling(List<Point2D> sample) {
        sample.forEach(e -> e.set(e.y, e.x));
        return Sampling.sampleToFunction(sample);
    }

    public static UnaryFunction byReSampling(Function2D f, double l, double u, double delta) {
        return byReSampling(Sampling.sample(f, l, u, delta));
    }
}
