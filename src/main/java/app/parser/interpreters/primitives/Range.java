package app.parser.interpreters.primitives;

import app.parser.interpreters.Primitive;
import app.parser.interpreters.PrimitiveID;

import java.math.BigDecimal;
import java.lang.Math;

public class Range extends Primitive {

    private final int start;
    private final int end;

    public Range(int s, int e) {
        start = s;
        end = e;
    }

    /* Base Methods */

    public String id() {
        return PrimitiveID.RANGE.name;
    }

    public String string() {
        return start + ":" + end;
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        Range r = Range.cast(p);
        return start == (r.start) && end == (r.end);
    }

    /* Logic Methods */

    public Range negate() {
        return new Range(end, start);
    }

    public int range() {
        return Math.abs(end - start);
    }

    public Primitive get(Num index) {
        if (!index.isInteger())
            return new Err("integers are required to index a matrix");

        int i = index.num().intValue();
        if (i > range())
            return new Err("out of bounds");

        return new Num(start + i);
    }

    public Integer compareTo(Primitive a) {
        if (Num.is(a))
            return BigDecimal.valueOf(range()).compareTo(Num.cast(a).num());

        if (Range.is(a))
            return BigDecimal.valueOf(range()).compareTo(BigDecimal.valueOf(Range.cast(a).range()));

        return null;
    }

    public int[] fullRange() {
        int[] range = new int[range()];
        if (start <= end) {
            for (int i = start; i < end; i++)
                range[i - start] = i;
        }
        else {
            for (int i = start; i > end; i--)
                range[start - i] = i;
        }

        return range;
    }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.RANGE.name);
    }

    public static Range cast(Primitive p) {
        if (!Range.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Range) p;
    }
}
