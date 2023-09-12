package resources.matrix.exceptions;

public class MatrixNullException extends MatrixException {

    public MatrixNullException() {
        super(MatrixExceptionMessage.NULL_ARGUMENT.name);
    }
}
