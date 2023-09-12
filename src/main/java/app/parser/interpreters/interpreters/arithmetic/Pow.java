package app.parser.interpreters.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.primitives.Null;
import app.parser.interpreters.primitives.Num;

public class Pow implements Interpreter {
    private final Interpreter i1;
    private final Interpreter i2;

    public Pow(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        // --------- Errors --------- \\

        if (Err.is(p1))
            return p1;
        if (Err.is(p2))
            return p2;

        if (!Num.is(p2))
            return new Err("exponent degree must be a number");
        if (Null.is(p1) || Null.is(p2))
            return new Err("imbalanced exponent");

        // --------- Computation --------- \\

        if (Num.is(p1)) {
            return ((Num) p1).power(p2).solve();
        }

        if (Mat.is(p1)) {
            return Mat.cast(p1).power(p2).solve();
        }

        return new Err("invalid exponent");
    }
}
