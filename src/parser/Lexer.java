package parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {
    private static final String OPERATOR_CHARS = "+-*/()=<>!&|;";

    private static final Map<String, TokenType> OPERATORS;
    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("(", TokenType.LBRACKET);
        OPERATORS.put(")", TokenType.RBRACKET);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("=", TokenType.EQUAL);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(";", TokenType.SEMICOLON);

        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("==", TokenType.DOUBLE_EQUAL);
        OPERATORS.put("!=", TokenType.EXCL_EQUAL);
        OPERATORS.put(">=", TokenType.LTEQ);
        OPERATORS.put("<=", TokenType.GTEQ);

        OPERATORS.put("&&", TokenType.DOUBLE_AMP);
        OPERATORS.put("||", TokenType.DOUBLE_BAR);

    }

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
            if (Character.isDigit(current))
                tokenizeNumber();
            else if (Character.isLetter(current))
                tokenizeWord();
            else if (current == '"')
                tokenizeString();
            else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }

        return tokens;
    }

    private void tokenizeOperator() {
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComments();
                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeBlockComments();
                return;
            }
        }

        final StringBuilder buffer = new StringBuilder();
        while (true) {
            final String text = buffer.toString();
            if (!OPERATORS.containsKey(text + current) && !text.isEmpty()) {
                addToken(OPERATORS.get(text));
                return;
            }
            buffer.append(current);
            current = next();
        }
    }

    private void tokenizeComments() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeBlockComments() {
        char current = peek(0);
        do {
            if (current == '\0')
                throw new RuntimeException("Unterminated block comment");
            current = next();
        } while (current != '*' || peek(1) != '/');

        next();
        next();
    }

    private void tokenizeWord() {
        final StringBuilder buffer = new StringBuilder();
        char current = peek(0);

        while (Character.isLetterOrDigit(current) || (current == '_') || (current == '$')) {
            buffer.append(current);
            current = next();
        }

        final String string = buffer.toString();
        switch (string) {
            case "print" -> {
                addToken(TokenType.PRINT);
                break;
            }
            case "if" -> {
                addToken(TokenType.IF);
                break;
            }
            case "else" -> {
                addToken(TokenType.ELSE);
                break;
            }
            case "while" -> {
                addToken(TokenType.WHILE);
                break;
            }
            case "for" -> {
                addToken(TokenType.FOR);
                break;
            }
            case "do" -> {
                addToken(TokenType.DO);
                break;
            }
            case "break" -> {
                addToken(TokenType.BREAK);
                break;
            }
            case "continue" -> {
                addToken(TokenType.CONTINUE);
                break;
            }
            default -> {
                addToken(TokenType.WORD, string);
                break;
            }
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

            if (current == '"')
                break;
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
                if (buffer.indexOf(".") != -1)
                    throw new RuntimeException("Invalid float number");
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
