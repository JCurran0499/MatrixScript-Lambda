package app.parser.interpreters.primitives;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.PrimitiveID;

import java.util.ArrayList;
import java.util.List;

public class Tuple extends Primitive {

    private final List<Primitive> pList;

    public Tuple(List<Interpreter> iList) {
        pList = new ArrayList<>();
        for (Interpreter i : iList)
            pList.add(i.solve());
    }

    public Tuple(Interpreter... iList) {
        pList = new ArrayList<>();
        for (Interpreter i : iList)
            pList.add(i.solve());
    }

    /* Base Methods */

    public Primitive solve() {
        for (Primitive p : pList) {
            if (Err.is(p))
                return p;
        }

        return this;
    }

    public String id() {
        return PrimitiveID.TUPLE.name;
    }

    public String string() {
        StringBuilder s = new StringBuilder("(");
        for (Primitive p : pList) {
            if (Mat.is(p)) s.append(Mat.cast(p).lineString());
            else s.append(p.string());

            s.append(", ");
        }

        s.delete(s.length() - 2, s.length());
        s.append(")");

        return s.toString();
    }

    public int length() {
        return pList.size();
    }

    public boolean equals(Primitive p) {
        if (!id().equals(p.id()))
            return false;

        Tuple t = Tuple.cast(p);
        if (pList.size() != t.pList.size())
            return false;

        for (int i = 0; i < pList.size(); i++) {
            if (!pList.get(i).equals(t.pList.get(i)))
                return false;
        }

        return true;
    }

    /* Logic Methods */

    public Tuple negate() {
        List<Interpreter> newPList = new ArrayList<>();
        for (int i = pList.size() - 1; i >= 0; i--)
            newPList.add(pList.get(i));

        return new Tuple(newPList);
    }

    public Primitive get(Primitive index) {
        if (Num.is(index))
            return get(Num.cast(index));

        else if (Range.is(index))
            return get(Range.cast(index));

        else if (Tuple.is(index))
            return get(Tuple.cast(index));

        else
            return new Err("invalid 'get' command on tuple");
    }

    public static boolean is(Primitive p) {
        return p.id().equals(PrimitiveID.TUPLE.name);
    }

    public static Tuple cast(Primitive p) {
        if (!Tuple.is(p))
            throw new ClassCastException("incompatible primitive cast");

        return (Tuple) p;
    }

    /* Helper Methods */

    private Primitive get(Num index) {
        if (!index.isInteger())
            return new Err("index must be integer");

        int i = index.num().intValue();
        if (i < 0 || i > length())
            return new Err("outside tuple bounds");

        return pList.get(i);

    }

    private Primitive get(Range range) {
        List<Interpreter> newTuple = new ArrayList<>();

        for (int i : range.fullRange()) {
            if (i < 0 || i >= length())
                return new Err("outside tuple bounds");

            newTuple.add(pList.get(i));
        }

        return new Tuple(newTuple);
    }

    private Primitive get(Tuple tuple) {
        List<Interpreter> newTuple = new ArrayList<>();

        for (Primitive p : tuple.pList) {
            if (!Num.is(p) || !Num.cast(p).isInteger())
                return new Err("index must be integer");

            int i = Num.cast(p).num().intValue();
            if (i < 0 || i >= length())
                return new Err("outside tuple bounds");

            newTuple.add(pList.get(i));
        }

        return new Tuple(newTuple);
    }
}
