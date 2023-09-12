package resources.matrix.exceptions;

public class MatrixOutOfBoundsException extends MatrixException {

    public MatrixOutOfBoundsException() {
        super(MatrixExceptionMessage.OUT_OF_BOUNDS.name);
    }
}
