package app.parser.interpreters.interpreters.arithmetic;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Num;

public class Fact implements Interpreter {
    private final Interpreter i;

    public Fact(Interpreter i) {
        this.i = i;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = i.solve();

        if (Err.is(p))
            return p;

        if (Num.is(p))
            return ((Num) p).factorial();

        return new Err("invalid factorial");
    }
}
