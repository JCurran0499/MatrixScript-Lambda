package app.parser.interpreters;

public enum PrimitiveID {
    BOOL("bool"),
    ERR("err"),
    MAT("mat"),
    NULL("null"),
    NUM("num"),
    RANGE("range"),
    TUPLE("tuple"),
    DECLARE("declare"),
    FUNCTION("function");

    public final String name;

    PrimitiveID(String id) {
        name = id;
    }
}
