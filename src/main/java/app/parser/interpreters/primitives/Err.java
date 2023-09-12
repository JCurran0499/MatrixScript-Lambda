package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;
import app.parser.interpreters.PrimitiveID;

public class Err extends Primitive {
    private final String message;

    public Err(String err) {
        message = err;
    }

    /* Base Methods */

    public String id() {
        return PrimitiveID.ERR.name;
    }

    public String string() {
        return "Error: " + message;
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return message.equals(Err.cast(p).message);
    }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.ERR.name);
    }

    public static Err cast(Primitive p) {
        if (!Err.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Err) p;
    }
}
