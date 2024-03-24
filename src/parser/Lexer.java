package parser;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/()=";
    private static final TokenType[] OPERATOR_TOKENS = {
            TokenType.PLUS, TokenType.MINUS,
            TokenType.STAR, TokenType.SLASH,
            TokenType.LBRACKET, TokenType.RBRACKET,
            TokenType.EQUAL
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

    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isDigit(current)) tokenizeNumber();
            else if (Character.isLetter(current)) tokenizeWord();
            else if (current == '"') tokenizeString();
            else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }

        return tokens;
    }

    private void tokenizeOperator() {
        final int position = OPERATOR_CHARS.indexOf(peek(0));
        addToken(OPERATOR_TOKENS[position]);
        next();
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (Character.isLetterOrDigit(current) || (current == '_') || (current == '$')) {
            buffer.append(current);
            current = next();
        }

        String string = buffer.toString();
        if (string.equals("print")) {
            addToken(TokenType.PRINT);
        } else {
            addToken(TokenType.WORD, string);
        }
    }

    private void tokenizeString() {
        next(); // skip '"'
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"' -> {
                        current = next();
                        buffer.append('"');
                        continue;
                    }
                    case 'n' -> {
                        current = next();
                        buffer.append('\n');
                        continue;
                    }
                    case 't' -> {
                        current = next();
                        buffer.append('\t');
                        continue;
                    }
                }
                buffer.append('\\');
                continue;
            }

            if (current == '"') break;
            buffer.append(current);
            current = next();
        }

        next(); // skip closing '"'
        addToken(TokenType.STRING, buffer.toString());
    }

    private void tokenizeNumber() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (true) {
            if (current == '.') {
                if (buffer.indexOf(".") != -1) throw new RuntimeException("Invalid float number");
            } else if (!Character.isDigit(current)) {
                break;
            }

            buffer.append(current);
            current = next();
        }
        addToken(TokenType.NUMBER, buffer.toString());
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition) {
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
