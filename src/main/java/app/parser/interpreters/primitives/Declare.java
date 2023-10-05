package app.parser.interpreters.primitives;

import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.Token;
import app.parser.interpreters.PrimitiveID;
import resources.aws.AwsService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Declare extends Primitive {

    private final String token;
    private final String varName;
    private final Primitive value;


    public Declare(String t, String v, Interpreter e) {
        token = t;
        varName = v;
        value = e.solve();
    }

    /* Base Methods */

    public Primitive solve() {
        if (!isValidVariable(varName))
            return new Err("invalid variable name");

        if (Err.is(value))
            return value;
        if (Null.is(value))
            return new Err("variable '" + varName + "' must be set to a value");

        addVariable(varName, value);
        return this;
    }

    public String id() {
        return PrimitiveID.DECLARE.name;
    }

    public String string() {
        return value.string();
    }

    public boolean equals(Primitive p) {
        return value.equals(p);
    }


    /* Logic Methods */
    public void addVariable(String varName, Primitive value) {
        AwsService.putAttribute(token, varName, value);
    }

    public static boolean is(Primitive p) {
    return p.id().equals(PrimitiveID.DECLARE.name);
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
