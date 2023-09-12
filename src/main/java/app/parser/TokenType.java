package app.parser;

public enum TokenType {
    PAREN, MAT,
    ADD, SUB, MULT, DIV, POW, FACT, MERGE, COMMA, NEG,
    EQUAL, GREAT, LESS, GTEQUAL, LTEQUAL, NOT,
    DECLARE, GET, FROM, SET, TO, DIM,
    NUM, BOOL, ERR,
    VAR
}
