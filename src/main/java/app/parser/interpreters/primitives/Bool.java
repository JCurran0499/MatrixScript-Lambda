package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;
import app.parser.interpreters.PrimitiveID;

public class Bool extends Primitive {

    private static final Bool trueBool = new Bool(true);
    private static final Bool falseBool = new Bool(false);

    private final boolean bool;

    private Bool(boolean b) {
        bool = b;
    }

    public static Bool of(boolean b) {
        return b
            ? trueBool
            : falseBool;
    }

    /* Base Methods */

    public String id() {
        return PrimitiveID.BOOL.name;
    }

    public String string() {
        return (bool ? "true" : "false");
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return bool == Bool.cast(p).bool;
    }

    /* Logic Methods */

    public boolean bool() {
        return bool;
    }

    public Bool not() {
        return Bool.of(!bool);
    }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.BOOL.name);
    }

    public static Bool cast(Primitive p) {
        if (!Bool.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Bool) p;
    }
}
