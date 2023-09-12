package app.parser.interpreters.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.*;

public class Neg implements Interpreter {

    private final Interpreter i;

    public Neg(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Num.is(p))
            return Num.cast(p).negate();

        if (Mat.is(p))
            return Mat.cast(p).negate();

        if (Range.is(p))
            return Range.cast(p).negate();

        if (Tuple.is(p))
            return Tuple.cast(p).negate();

        return new Err("cannot negate this command");
    }
}
