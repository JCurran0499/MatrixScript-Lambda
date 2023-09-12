package resources.matrix.exceptions;

public abstract class MatrixException extends RuntimeException {

    public MatrixException(String errMessage) {
        super(errMessage);
    }
}
