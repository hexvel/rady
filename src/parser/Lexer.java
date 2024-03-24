package parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/()";
    private static final TokenType[] OPERATOR_TOKENS = {
            TokenType.PLUS, TokenType.MINUS,
            TokenType.STAR, TokenType.SLASH,
            TokenType.LBRACKET, TokenType.RBRACKET
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
            if (Character.isDigit(current)) TokenizeNumber();
            else if (Character.isLetter(current)) TokenizeWord();
            else if (OPERATOR_CHARS.indexOf(current) != -1) {
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

    private void TokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = Peek(0);

        while (Character.isLetterOrDigit(current) || (current == '_') || (current == '$')) {
            buffer.append(current);
            current = Next();
        }
        addToken(TokenType.WORD, buffer.toString());
    }

    private void TokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = Peek(0);

        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid float number");
            } else if (!Character.isDigit(current)) {
                break;
            }

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
