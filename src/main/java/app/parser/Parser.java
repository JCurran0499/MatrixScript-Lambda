package app.parser;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import app.parser.interpreters.Interpreter;
import app.parser.interpreters.Primitive;
import app.parser.interpreters.interpreters.arithmetic.*;
import app.parser.interpreters.interpreters.commands.*;
import app.parser.interpreters.interpreters.comparisons.*;
import app.parser.interpreters.primitives.*;
import app.parser.interpreters.variables.Var;
import resources.matrix.exceptions.MatrixDimensionsException;
import resources.matrix.Matrix;

public class Parser {
    public static Interpreter parse(String sessionToken, String command) {
        if (command.isEmpty())
            return Null.instance();

        return parseTokens(sessionToken, tokenize(command));
    }

    private static ArrayList<Token> tokenize(String command) {
        command = command.strip();

        ArrayList<Token> tokenList = new ArrayList<>();
        Token token;
        boolean addedToken;

        while (command.length() > 0) {
            addedToken = false;

            // ---------- Parentheses ---------- \\
            if (command.startsWith("(")) {
                token = Token.parseBracket(command, '(', ')', TokenType.PAREN);
                if (token.type() == TokenType.ERR) {
                    tokenList.add(new Token(TokenType.ERR, "imbalanced parentheses"));
                    command = command.substring(token.value().length() + 1).stripLeading();
                }
                else {
                    tokenList.add(token);
                    command = command.substring(token.value().length() + 2).stripLeading();
                }
                addedToken = true;
            }

            // ---------- Matrix ---------- \\
            else if (command.startsWith("[")) {
                token = Token.parseBracket(command, '[', ']', TokenType.MAT);
                if (token.type() == TokenType.ERR) {
                    tokenList.add(new Token(TokenType.ERR, "imbalanced matrix brackets"));
                    command = command.substring(token.value().length() + 1).stripLeading();
                }
                else {
                    tokenList.add(token);
                    command = command.substring(token.value().length() + 2).stripLeading();
                }
                addedToken = true;
            }

            // ---------- Subtraction & Negation ---------- \\
            else if (command.startsWith("-")) {
                TokenType prevType = tokenList.isEmpty() ? null : tokenList.get(tokenList.size() - 1).type();
                if (Token.subtractables.contains(prevType))
                    tokenList.add(new Token(TokenType.SUB, "-"));
                else
                    tokenList.add(new Token(TokenType.NEG, "-"));

                addedToken = true;
                command = command.substring(1).stripLeading();
            }

            // ---------- Key Words & Symbols ---------- \\
            else {
                for (TokenType type : Token.equivalentSymbols.keySet()) {
                    Matcher m = Token.equivalentSymbols.get(type).matcher(command);
                    if (m.find() && m.start() == 0) {
                        int g = 0; // identify which group in the regex was matched
                        for (int i = 1; i <= m.groupCount(); i++)
                            if (m.start(i) > -1) g = i;

                        token = new Token(type, m.group(g));
                        tokenList.add(token);
                        addedToken = true;

                        command = command.substring(m.end(g)).stripLeading();
                        break;
                    }
                }
            }

            // ---------- Variable Names & Errors ---------- \\
            if (!addedToken) {
                Matcher m = Pattern.compile("[a-zA-Z_]\\w*").matcher(command);
                if (m.find() && m.start() == 0) {
                    tokenList.add(new Token(TokenType.VAR, m.group()));
                    command = command.substring(m.end()).stripLeading();
                }

                // error text
                else {
                    StringBuilder errorText = new StringBuilder();
                    List<Character> stoppers = Arrays.asList(' ', '\t', '+', '-', '*', '/', '^', '!', ':',
                            '=', '<', '>', '~', ',', '(', '[');

                    while (command.length() > 0 && !stoppers.contains(command.charAt(0))) {
                        errorText.append(command.charAt(0));
                        command = command.substring(1).stripLeading();
                    }

                    tokenList.add(new Token(TokenType.ERR, errorText.toString()));
                }
            }
        }

        return tokenList;
    }

    private static Interpreter parseTokens(String sessionToken, List<Token> tokens) {
        int index;

        // ---------- PRIMITIVES ---------- \\

        if (tokens.isEmpty())
            return Null.instance();

        if (tokens.size() == 1) {
            Token t = tokens.get(0);
            if (t.type() == TokenType.NUM)
                return new Num(new BigDecimal(t.value()));
            else if (t.type() == TokenType.BOOL)
                return Bool.of(t.value().strip().equals("true"));
            else if (t.type() == TokenType.MAT)
                return parseMatrixTokens(sessionToken, t.value());
            else if (t.type() == TokenType.PAREN)
                return parseTokens(sessionToken, tokenize(t.value()));
            else if (t.type() == TokenType.VAR)
                return new Var(sessionToken, t.value());
            else if (t.type() == TokenType.ERR)
                return new Err(t.value());
        }

        // ---------- COMMANDS ---------- \\

        index = findFirstToken(tokens, TokenType.DECLARE, 0);
        if (index > -1) {
            if (index != 1)
                return new Err("invalid variable declaration");

            return new Declare(sessionToken, tokens.get(0).value(),
                    parseTokens(sessionToken, tokens.subList(2, tokens.size())));
        }

        index = findLastToken(tokens, TokenType.FROM);
        if (tokens.get(0).type() == TokenType.GET && index > 0)
            return new Get(parseTokens(sessionToken, tokens.subList(1, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        if (tokens.get(0).type() == TokenType.DIM)
            return new Dim(parseTokens(sessionToken, tokens.subList(1, tokens.size())));

        // ---------- TUPLES ---------- \\

        index = findFirstToken(tokens, TokenType.COMMA, 0);
        if (index > -1) {
            List<Interpreter> tupleValues = new ArrayList<>();
            int prevIndex = -1;

            while (index > -1) {
                tupleValues.add(parseTokens(sessionToken, tokens.subList(prevIndex + 1, index)));

                prevIndex = index;
                index = findFirstToken(tokens, TokenType.COMMA, index + 1);
            }
            tupleValues.add(parseTokens(sessionToken, tokens.subList(prevIndex + 1, tokens.size())));

            return new Tuple(tupleValues);
        }

        // ---------- COMPARISONS ---------- \\

        index = findFirstToken(tokens, TokenType.EQUAL, 0);
        if (index > -1)
            return new Equal(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.GREAT, 0);
        if (index > -1)
            return new Great(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.LESS, 0);
        if (index > -1)
            return new Less(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.GTEQUAL, 0);
        if (index > -1)
            return new GTEqual(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.LTEQUAL, 0);
        if (index > -1)
            return new LTEqual(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        // ---------- ARITHMETIC ---------- \\

        index = findLastToken(tokens, TokenType.ADD);
        if (index > -1)
            return new Add(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.SUB);
        if (index > -1)
            return new Sub(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.MULT);
        if (index > -1)
            return new Mult(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findLastToken(tokens, TokenType.DIV);
        if (index > -1)
            return new Div(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.MERGE, 0);
        if (index > -1)
            return new Merge(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        index = findFirstToken(tokens, TokenType.POW, 0);
        if (index > -1)
            return new Pow(parseTokens(sessionToken, tokens.subList(0, index)),
                    parseTokens(sessionToken, tokens.subList(index + 1, tokens.size())));

        if (tokens.get(tokens.size() - 1).type() == TokenType.FACT)
            return new Fact(parseTokens(sessionToken, tokens.subList(0, tokens.size() -1)));

        if (tokens.get(0).type() == TokenType.NEG)
            return new Neg(parseTokens(sessionToken, tokens.subList(1, tokens.size())));

        if (tokens.get(0).type() == TokenType.NOT)
            return new Not(parseTokens(sessionToken, tokens.subList(1, tokens.size())));

        // ---------- ERROR ---------- \\

        return new Err("command not recognized");
    }

    // -------------- HELPER METHODS -------------- \\

    private static List<String> spaceSplit(String command) {
        command = command.strip();
        Pattern p = Pattern.compile("\\S+");
        String s;

        List<String> commandParts = new ArrayList<>();
        while (command.length() > 0) {
            if (command.startsWith("("))
                s = "(" + Token.parseBracket(command, '(', ')', TokenType.PAREN).value() + ")";

            else if (command.startsWith("["))
                s = "[" + Token.parseBracket(command, '[', ']', TokenType.MAT).value() + "]";

            else {
                Matcher m = p.matcher(command);
                if (m.find())
                    s = m.group();
                else
                    s = "";
            }

            commandParts.add(s);
            command = command.substring(s.length()).stripLeading();
        }

        return commandParts;
    }

    private static Primitive parseMatrixTokens(String sessionToken, String m) {
        List<List<BigDecimal>> matrixList = new ArrayList<>();

        // break into rows
        String[] rows = m.split(";");
        for (String s : rows) {
            matrixList.add(new ArrayList<>());
            List<BigDecimal> row = matrixList.get(matrixList.size() - 1);

            // break into individual items in the row
            List<String> rowItems = spaceSplit(s);
            for (String item : rowItems) {
                Primitive itemValue = parseTokens(sessionToken, tokenize(item)).solve();

                // add each item to the matrix
                if (Num.is(itemValue)) {
                    row.add(Num.cast(itemValue).num());
                } else if (Mat.is(itemValue)) {
                    matrixList.remove(matrixList.size() - 1);
                    BigDecimal[][] mat = Mat.cast(itemValue).mat().mapToArray(new BigDecimal(0), BigDecimal::valueOf);
                    for (BigDecimal[] r : mat)
                        matrixList.add(Arrays.asList(r));
                } else if (Range.is(itemValue)) {
                    for (int v : Range.cast(itemValue).fullRange()) row.add(BigDecimal.valueOf(v));
                } else if (Err.is(itemValue)) {
                    return itemValue;
                } else return new Err("invalid matrix");
            }
        }

        BigDecimal[][] matrixArray = new BigDecimal[matrixList.size()][];
        for (int i = 0; i < matrixList.size(); i++)
            matrixArray[i] = matrixList.get(i).toArray(new BigDecimal[0]);

        try {
            return new Mat(new Matrix(matrixArray));
        } catch (MatrixDimensionsException e) {
            return new Err("invalid matrix dimensions");
        }
    }

    private static int findFirstToken(List<Token> tokens, TokenType type, int start) {
        for (int i = start; i < tokens.size(); i++)
            if (tokens.get(i).type() == type)
                return i;

        return -1;
    }

    private static int findLastToken(List<Token> tokens, TokenType type) {
        for (int i = tokens.size() - 1; i >= 0; i--)
            if (tokens.get(i).type() == type)
                return i;

        return -1;
    }
}
