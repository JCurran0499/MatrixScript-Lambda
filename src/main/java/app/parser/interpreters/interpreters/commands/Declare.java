package app.parser.interpreters.interpreters.commands;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.primitives.Err;
import app.parser.interpreters.primitives.Null;
import app.parser.interpreters.variables.SessionHandler;
import app.parser.Token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Declare implements Interpreter {

    private final String token;
    private final String varName;
    private final Interpreter expression;


    public Declare(String t, String v, Interpreter e) {
        token = t;
        varName = v;
        expression = e;
    }

    /* Base Methods */

    public Primitive solve() {
        if (!isValidVariable(varName))
            return new Err("invalid variable name");

        Primitive p = expression.solve();

        if (Err.is(p))
            return p;
        if (Null.is(p))
            return new Err("variable '" + varName + "' must be set to a value");

        p.printValue = false;

        addVariable(varName, p);
        return p;
    }


    /* Logic Methods */
    public void addVariable(String varName, Primitive value) {
        SessionHandler.setVar(token, varName, value);
    }

    private static boolean isValidVariable(String name) {
        for (Pattern p : Token.equivalentSymbols.values())
            if (p.matcher(name).matches())
                return false;

        Pattern validVariable = Pattern.compile("[a-zA-Z_]\\w*");
        Matcher matcher = validVariable.matcher(name);
        return matcher.matches();
    }
}
