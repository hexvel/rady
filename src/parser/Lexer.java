package parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/";
    private static final TokenType[] OPERATOR_TOKENS = {
        TokenType.PLUS, TokenType.MINUS,
        TokenType.STAR, TokenType.SLASH
    };

    private final String input;
    private final int length;
    private final List<Token> tokens;
    private int pos;

    public Lexer(String input) {
        this.input = input;
        length = input.length();
        tokens = new ArrayList<>();
    }

    public List<Token> Tokenize() {
        while (pos < length) {
            final char current = Peek(0);
            if (Character.isDigit(current)) {
                TokenizeNumber();
            } else if (OPERATOR_CHARS.indexOf(current) != -1) {
             TokenizeOperator();
            } else {
                Next();
            }
        }

        return tokens;
    }

    private void TokenizeOperator() {
        final int position = OPERATOR_CHARS.indexOf(Peek(0));
        addToken(OPERATOR_TOKENS[position]);
        Next();
    }

    private void TokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = Peek(0);
        while (Character.isDigit(current)) {
            buffer.append(current);
            current = Next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private char Next() {
        pos++;
        return Peek(0);
    }

    private char Peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) {
            return '\0';
        }
        return input.charAt(position);
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String text) {
        tokens.add(new Token(type, text));
    }
}
