package app.parser.interpreters.variables;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import resources.aws.AwsService;

public class Var implements Interpreter {

    private final String token;
    private final String name;

    public Var(String t, String n) {
        token = t;
        name = n;
    }

    /* Base Methods */

    public Primitive solve() {
        Primitive p = AwsService.getAttribute(token, name);
        if (p == null)
            return new Err("variable '" + name + "' does not exist");

        return p;
    }
}
