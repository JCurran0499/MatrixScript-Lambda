package resources.matrix.exceptions;

public class MatrixDimensionsException extends MatrixException {

    public MatrixDimensionsException() {
        super(MatrixExceptionMessage.INVALID_DIMENSIONS.name);
    }
}
