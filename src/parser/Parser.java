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

    public List<Statement> parse() {
        final List<Statement> result = new ArrayList<>();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }

    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }
        if (match(TokenType.IF)) {
            return conditionalKeywords();
        }
        return assignmentStatement();
    }

    private Statement assignmentStatement() {
        // WORD EQUAL
        final Token current = get(0);
        if (match(TokenType.WORD) && get(0).getType() == TokenType.EQUAL) {
            final String variable = current.getText();
            consume(TokenType.EQUAL);
            return new AssignmentStatement(variable, expression());
        }

        throw new RuntimeException("Unknown statement");
    }

    private Statement conditionalKeywords() {
        final Expression condition = expression();
        final Statement ifStatement = statement();
        final Statement elseStatement;

        if (match(TokenType.ELSE)) {
            elseStatement = statement();
        } else {
            elseStatement = null;
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Expression expression() {
        return conditional();
    }

    private Expression conditional() {
        Expression result = additive();

        while (true) {
            if (match(TokenType.EQUAL)) {
                result = new ConditionalExpression('=', result, additive());
                continue;
            }
            if (match(TokenType.LT)) {
                result = new ConditionalExpression('<', result, additive());
                continue;
            }
            if (match(TokenType.GT)) {
                result = new ConditionalExpression('>', result, additive());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression additive() {
        Expression result = multiplicative();

        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression('+', result, multiplicative());
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression('-', result, multiplicative());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression multiplicative() {
        Expression result = unary();

        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression('*', result, unary());
                continue;
            }
            if (match(TokenType.SLASH)) {
                result = new BinaryExpression('/', result, unary());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression unary() {
        if (match(TokenType.MINUS)) {
            return new UnaryExpression('-', primary());
        }
        return primary();
    }

    private Expression primary() {
        final Token current = get(0);
        if (match(TokenType.NUMBER)) {
            return new ValueExpression(Double.parseDouble(current.getText()));
        }
        if (match(TokenType.WORD)) {
            return new VariableExpression(current.getText());
        }
        if (match(TokenType.STRING)) {
            return new ValueExpression(current.getText());
        }
        if (match(TokenType.LBRACKET)) {
            Expression result = expression();
            match(TokenType.RBRACKET);
            return result;
        }
        throw new RuntimeException("Unknown expression");
    }

    private void consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) throw new RuntimeException("Token" + current + " doesn't math " + type);
        pos++;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) return false;

        pos++;
        return true;
    }

    private Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) {
            return EOF;
        }
        return tokens.get(position);
    }
}
