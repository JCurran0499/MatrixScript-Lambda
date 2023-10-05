package app.parser.interpreters;

import java.io.Serializable;

public abstract class Primitive implements Interpreter, Serializable {

    public Primitive solve() {
        return this;
    }

    public abstract String id();
    
    public abstract String string();

    public abstract boolean equals(Primitive p);
}
