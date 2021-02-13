package jmath.datatypes.functions;

import java.io.Serializable;

@FunctionalInterface
@SuppressWarnings("unused")
public interface Function<Y, X> extends Serializable {
    Y valueAt(X x);
    default Y atOrigin() {return null;}
    default Y valueAt_(Object x) {
        //noinspection unchecked
        return valueAt((X) x);
    }
}
