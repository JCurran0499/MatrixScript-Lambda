package resources.matrix.exceptions;

public enum MatrixExceptionMessage {
    INVALID_DIMENSIONS("Invalid dimensions for defining a matrix"),
    INVALID_STRING("Invalid string for defining a matrix"),
    NULL_ARGUMENT("Invalid argument: null"),
    OUT_OF_BOUNDS("Out of bounds for this matrix");

    public final String name;
    MatrixExceptionMessage(String message) {
        name = message;
    }

}
