package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;
import java.math.BigDecimal;
import java.math.MathContext;

import app.parser.interpreters.PrimitiveID;
import resources.matrix.Matrix;

public class Num extends Primitive {
    private final BigDecimal num;

    public Num(BigDecimal n) {
        num = n;
    }

    public Num(double n) {
        num = BigDecimal.valueOf(n);
    }

    /* Base Methods */

    public String id() {
        return PrimitiveID.NUM.name;
    }

    public String string() {
        return num.toString();
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        return num.compareTo(Num.cast(p).num) == 0;
    }

    /* Logic Methods */

    public BigDecimal num() {
        return num;
    }

    public Primitive add(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num;
            return new Num(num.add(n));
        }

        else return new Err("invalid addition");
    }

    public Primitive subtract(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num;
            return new Num(num.subtract(n));
        }

        else return new Err("invalid subtraction");
    }

    public Primitive multiply(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num;
            return new Num(num.multiply(n));
        }

        else if (Mat.is(a)) {
            Matrix m = Mat.cast(a).mat();
            return new Mat(m.multiply(num.doubleValue()));
        }

        else return new Err("invalid multiplication");
    }

    public Primitive divide(Primitive a) {
        if (Num.is(a)) {
            BigDecimal n = Num.cast(a).num;
            try {
                return new Num(num.divide(n, MathContext.DECIMAL128));
            } catch (ArithmeticException e) {
                return new Err("cannot divide by 0");
            }
        }

        else return new Err("invalid division");
    }

    public Primitive power(Primitive a) {
        if (Num.is(a) && Num.cast(a).isInteger()) {
            BigDecimal n = Num.cast(a).num;
            return new Num(num.pow(n.intValue()));
        }

        else return new Err("exponent must be an integer");
    }

    public Num negate() {
        return new Num(num.negate());
    }

    public Primitive factorial() {
        if (isInteger() && isPositive()) {
            BigDecimal factorial = BigDecimal.valueOf(1);
            for (BigDecimal b = num; b.compareTo(BigDecimal.ZERO) > 0; b = b.subtract(BigDecimal.ONE))
                factorial = factorial.multiply(b);

            return new Num(factorial);
        }

        return new Err("factorial must be a positive integer");
    }

    public Integer compareTo(Primitive a) {
        if (Num.is(a))
            return num.compareTo(Num.cast(a).num);

        if (Range.is(a))
            return num.compareTo(BigDecimal.valueOf(Range.cast(a).range()));

        return null;
    }

    public boolean isInteger() {
        return (num.doubleValue() % 1 == 0);
    }

    public boolean isPositive() { return (num.doubleValue() > 0); }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.NUM.name);
    }

    public static Num cast(Primitive p) {
        if (!Num.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Num) p;
    }
}
