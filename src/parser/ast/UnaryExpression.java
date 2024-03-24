package parser.ast;

import lib.NumberValue;
import lib.Value;

public class UnaryExpression implements Expression {
    private final Expression expr;
    private final char operation;

    public UnaryExpression(char operation, Expression expr) {
        this.operation = operation;
        this.expr = expr;
    }

    @Override
    public Value eval() {
        return switch (operation) {
            case '-' -> new NumberValue(-expr.eval().asDouble());
            default -> expr.eval();
        };
    }

    @Override
    public String toString() {
        return String.format("%c %s", operation, expr);
    }
}
