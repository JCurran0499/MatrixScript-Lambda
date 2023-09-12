package app.parser.interpreters.interpreters.comparisons;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Bool;
import app.parser.interpreters.primitives.Err;

public class Not implements Interpreter {

    private final Interpreter i;

    public Not(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Bool.is(p)) {
            return Bool.cast(p).not();
        }

        return new Err("can only reverse booleans");
    }
}
