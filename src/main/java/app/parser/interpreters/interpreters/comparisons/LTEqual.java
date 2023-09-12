package app.parser.interpreters.interpreters.comparisons;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Bool;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Num;
import app.parser.interpreters.primitives.Range;

public class LTEqual implements Interpreter {

    private final Interpreter i1;
    private final Interpreter i2;

    public LTEqual(Interpreter i1, Interpreter i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p1 = i1.solve();
        Primitive p2 = i2.solve();

        if (Err.is(p1))
            return p1;
        if (Err.is(p2))
            return p2;

        Integer comparison = null;
        if (Num.is(p1))
            comparison = ((Num) p1).compareTo(p2);
        else if (Range.is(p1))
            comparison = ((Range) p1).compareTo(p2);

        if (comparison == null)
            return new Err("invalid comparison");

        return Bool.of(comparison <= 0);
    }
}
