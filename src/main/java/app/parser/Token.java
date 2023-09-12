package app.parser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Token {
    private final TokenType type;
    private final String value;

    public static final Map<TokenType, Pattern> equivalentSymbols = Stream.of(new Object[][]{
            {TokenType.ADD, Pattern.compile("(\\+)")}, {TokenType.MULT, Pattern.compile("(\\*)")},
            {TokenType.DIV, Pattern.compile("(/)")}, {TokenType.POW, Pattern.compile("(\\^)")},
            {TokenType.FACT, Pattern.compile("(!)")}, {TokenType.MERGE, Pattern.compile("(:)")},
            {TokenType.COMMA, Pattern.compile("(,)")},
            {TokenType.EQUAL, Pattern.compile("(==)")}, {TokenType.GTEQUAL, Pattern.compile("(>=)")},
            {TokenType.LTEQUAL, Pattern.compile("(<=)")}, {TokenType.GREAT, Pattern.compile("(>)[^=]|(>)$")},
            {TokenType.LESS, Pattern.compile("(<)[^=]|(<)$")}, {TokenType.NOT, Pattern.compile("(~)")},
            {TokenType.DECLARE, Pattern.compile("(=)[^=]|(=)$")},
            {TokenType.GET, Pattern.compile("(get)\\s|(get)$")},
            {TokenType.FROM, Pattern.compile("(from)\\s|(from)$")},
            {TokenType.SET, Pattern.compile("(set)\\s|(set)$")},
            {TokenType.TO, Pattern.compile("(to)\\s|(to)$")},
            {TokenType.DIM, Pattern.compile("(dim)\\s|(dim)$")},
            {TokenType.NUM, Pattern.compile("(\\d+\\.\\d+|\\.\\d+|\\d+\\.|\\d+)")},
            {TokenType.BOOL, Pattern.compile("(true)\\s|(false)\\s|(true)$|(false)$")}
    }).collect(Collectors.toMap(t -> (TokenType)t[0], t -> (Pattern)t[1]));

    public static final List<TokenType> subtractables = Arrays.asList(
            TokenType.PAREN, TokenType.MAT, TokenType.NUM, TokenType.FACT,
            TokenType.BOOL, TokenType.VAR);

    public Token(TokenType t, String v) {
        type = t;
        value = v;
    }

    public TokenType type() { return type; }
    public String value() { return value; }


    public static Token parseBracket(String command, char open, char close, TokenType type) {
        StringBuilder phrase = new StringBuilder();
        int depth = 0;
        int index = 0;
        do {
            char c = command.charAt(index);
            if (c == open) { depth++; }
            if (c == close) { depth--; }

            phrase.append(c);
            index++;
        } while (depth > 0 && index < command.length());

        String phraseString = phrase.toString();
        if (depth != 0)
            return new Token(TokenType.ERR, phraseString.substring(1));
        else
            return new Token(type, phraseString.substring(1, phraseString.length() - 1));
    }
}
