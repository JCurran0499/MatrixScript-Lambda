package app.parser.interpreters.interpreters.commands;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Mat;
import app.parser.interpreters.primitives.Tuple;

public class Get implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public Get(Interpreter i1, Interpreter i2) {
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

        // --------- Computation --------- \\
        if (Tuple.is(p1) && Mat.is(p2)) {
            return Mat.cast(p2).get(Tuple.cast(p1));
        }

        if (Tuple.is(p2))
            return Tuple.cast(p2).get(p1);

        return new Err("invalid 'get' command");
    }
}
