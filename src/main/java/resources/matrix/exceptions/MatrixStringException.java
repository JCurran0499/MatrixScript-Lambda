package resources.matrix.exceptions;

public class MatrixStringException extends MatrixException {

    public MatrixStringException() {
        super(MatrixExceptionMessage.INVALID_STRING.name);
    }
}
