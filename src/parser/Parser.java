package parser;

import parser.ast.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final Token EOF = new Token(TokenType.EOF, "");
    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public List<Expression> Parse() {
        final List<Expression> result = new ArrayList<>();
        while (!Match(TokenType.EOF)) {
            result.add(expression());
        }

        return result;
    }

    private Expression expression() {
        return Additive();
    }

    private Expression Additive() {
        Expression result = Multiplicative();

        while (true) {
            if (Match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, Multiplicative());
                continue;
            }
            if (Match(TokenType.MINUS)) {
                result = new BinaryExpression('-', result, Multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression Multiplicative() {
        Expression result = Unary();

        while (true) {
            if (Match(TokenType.STAR)) {
                result = new BinaryExpression('*', result, Unary());
                continue;
            }
            if (Match(TokenType.SLASH)) {
                result = new BinaryExpression('/', result, Unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression Unary() {
        if (Match(TokenType.MINUS)) {
            return new UnaryExpression('-', Primary());
        }
        return Primary();
    }

    private Expression Primary() {
        final Token current = Get(0);
        if (Match(TokenType.NUMBER)) {
            return new NumberExpression(Double.parseDouble(current.getText()));
        }
        if (Match(TokenType.WORD)) {
            return new ConstantExpression(current.getText());
        }
        if (Match(TokenType.LBRACKET)) {
            Expression result = expression();
            Match(TokenType.RBRACKET);
            return result;
        }
        throw new RuntimeException("Unknown expression");
    }

    private boolean Match(TokenType type) {
        final Token current = Get(0);
        if (type != current.getType()) return false;

        pos++;
        return true;
    }

    private Token Get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) {
            return EOF;
        }
        return tokens.get(position);
    }
}
