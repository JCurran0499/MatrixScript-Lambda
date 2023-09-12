package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;
import app.parser.interpreters.PrimitiveID;
import resources.matrix.Matrix;
import java.math.BigDecimal;

public class Mat extends Primitive {
    private final Matrix mat;

    public Mat(Matrix m) {
        mat = m;
    }

    /* Base Methods */

    public Primitive solve() {
        if (mat == null)
            return new Err("invalid matrix");

        return this;
    }

    public String id() {
        return PrimitiveID.MAT.name;
    }

    public String string() {
        return mat.printString();
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return mat.equals(Mat.cast(p).mat);
    }

    /* Logic Methods */

    public Matrix mat() {
        return mat;
    }

    public String lineString() {
        return mat.toString();
    }

    public Primitive add(Primitive a) {
        if (Mat.is(a)) {
            Matrix m = Mat.cast(a).mat;
            return new Mat(mat.add(m));
        }

        else return new Err("invalid addition");
    }

    public Primitive subtract(Primitive a) {
        if (Mat.is(a)) {
            Matrix m = Mat.cast(a).mat;
            return new Mat(mat.subtract(m));
        }

        else return new Err("invalid subtraction");
    }

    public Primitive multiply(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num();
            return new Mat(mat.multiply(n.doubleValue()));
        }

        else if (Mat.is(a)) {
            Matrix m = Mat.cast(a).mat();
            return new Mat(mat.multiply(m));
        }

        else return new Err("invalid multiplication");
    }

    public Primitive divide(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num();
            return new Mat(mat.divide(n.doubleValue()));
        }

        else return new Err("invalid division");
    }

    public Primitive power(Primitive a) {
        if (Num.is(a) && Num.cast(a).isInteger()) {
            BigDecimal n = Num.cast(a).num();
            return new Mat(mat.toPower(n.intValue()));
        }

        else return new Err("exponent must be an integer");
    }

    public Primitive get(Tuple t) {
        if (t.length() != 2)
            return new Err("invalid 'get' command");

        if (Num.is(t.get(new Num(0))) && Num.is(t.get(new Num(1)))) {
            Num r = Num.cast(t.get(new Num(0)));
            Num c = Num.cast(t.get(new Num(1)));
            if (!r.isInteger() || !c.isInteger())
                return new Err("integers are required to index a matrix");

            int rint = r.num().intValue();
            int cint = c.num().intValue();
            if (rint < 0 || rint >= mat.rows() || cint < 0 || cint >= mat.cols())
                return new Err("out of matrix bounds");

            return new Num(mat.getValue(rint, cint));
        }

        return new Err("invalid 'get' command");
    }

    public Mat negate() {
        return new Mat(mat.multiply(-1));
    }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.MAT.name);
    }

    public static Mat cast(Primitive p) {
        if (!Mat.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Mat) p;
    }
}
