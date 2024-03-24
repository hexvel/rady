package parser;

import java.util.List;

import parser.ast.AssignmentStatement;
import parser.ast.BinaryExpression;
import parser.ast.BlockStatement;
import parser.ast.BreakStatement;
import parser.ast.ConditionalExpression;
import parser.ast.Expression;
import parser.ast.ForStatement;
import parser.ast.IfStatement;
import parser.ast.PrintStatement;
import parser.ast.Statement;
import parser.ast.UnaryExpression;
import parser.ast.ValueExpression;
import parser.ast.VariableExpression;
import parser.ast.WhileStatement;

public class Parser {
    private static final Token EOF = new Token(TokenType.EOF, "");
    private final List<Token> tokens;
    private final int size;

    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        size = tokens.size();
    }

    public Statement parse() {
        final BlockStatement result = new BlockStatement();
        while (!match(TokenType.EOF)) {
            result.add(statement());
        }

        return result;
    }

    private Statement block() {
        final BlockStatement blockStatement = new BlockStatement();
        consume(TokenType.LBRACE);
        while (!match(TokenType.RBRACE)) {
            blockStatement.add(statement());
        }

        return blockStatement;
    }

    private Statement statementOrBlock() {
        if (get(0).getType() == TokenType.LBRACE)
            return block();
        return statement();
    }

    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return new PrintStatement(expression());
        }
        if (match(TokenType.IF)) {
            return conditionalKeywords();
        }
        if (match(TokenType.WHILE)) {
            return whileStatement();
        }
        if (match(TokenType.FOR)) {
            return forStatement();
        }
        if (match(TokenType.BREAK)) {
            return new BreakStatement();
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
        final Statement ifStatement = statementOrBlock();
        final Statement elseStatement;

        if (match(TokenType.ELSE)) {
            elseStatement = statementOrBlock();
        } else {
            elseStatement = null;
        }

        return new IfStatement(condition, ifStatement, elseStatement);
    }

    private Statement whileStatement() {
        final Expression condition = expression();
        final Statement statement = statementOrBlock();
        return new WhileStatement(condition, statement);
    }

    private Statement forStatement() {
        final Statement initialize = assignmentStatement();
        consume(TokenType.SEMICOLON);
        final Expression termination = expression();
        consume(TokenType.SEMICOLON);
        final Statement increment = assignmentStatement();
        final Statement statement = statementOrBlock();
        return new ForStatement(initialize, termination, increment, statement);
    }

    private Expression expression() {
        return logicalOr();
    }

    private Expression logicalOr() {
        Expression result = logicalAnd();

        while (true) {
            if (match(TokenType.DOUBLE_BAR)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.OR, result, logicalAnd());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression logicalAnd() {
        Expression result = equality();

        while (true) {
            if (match(TokenType.DOUBLE_AMP)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.AND, result, equality());
                continue;
            }
            break;
        }

        return result;
    }

    private Expression equality() {
        Expression result = conditional();

        if (match(TokenType.DOUBLE_EQUAL)) {
            return new ConditionalExpression(ConditionalExpression.Operator.EQUAL, result, conditional());
        }

        if (match(TokenType.EXCL_EQUAL)) {
            return new ConditionalExpression(ConditionalExpression.Operator.NOT_EQUAL, result, conditional());
        }

        return result;
    }

    private Expression conditional() {
        Expression result = additive();

        while (true) {
            if (match(TokenType.LT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LT, result, additive());
                continue;
            }
            if (match(TokenType.GT)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GT, result, additive());
                continue;
            }
            if (match(TokenType.LTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.LTEQ, result, additive());
                continue;
            }
            if (match(TokenType.GTEQ)) {
                result = new ConditionalExpression(ConditionalExpression.Operator.GTEQ, result, additive());
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
        if (type != current.getType())
            throw new RuntimeException("Token" + current + " doesn't math " + type);
        pos++;
    }

    private boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType())
            return false;

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
