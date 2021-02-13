package jmath.functions.unaries.real;

import jmath.datatypes.functions.UnaryFunction;

import java.util.Arrays;

@SuppressWarnings("unused")
public class Rational extends UnaryFunction {
    private Rational(int degreeOfNumerator, double... coefficients) {
        super(x -> {
            double[] numerator = Arrays.copyOfRange(coefficients, 0, degreeOfNumerator + 1);
            double[] denominator = Arrays.copyOfRange(coefficients, degreeOfNumerator + 1, coefficients.length);
            return PolynomialFunction2D.f(denominator).fractionUnder(PolynomialFunction2D.f(numerator)).valueAt(x);
        });
    }

    public static UnaryFunction f(int degreeOfNumerator, double... coefficients) {
        return new Rational(degreeOfNumerator, coefficients);
    }
}
