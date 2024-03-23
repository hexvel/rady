package parser.ast;

public class UnaryExpression implements Expression {
    private final Expression expr;
    private final char operation;

    public UnaryExpression(char operation, Expression expr) {
        this.operation = operation;
        this.expr = expr;
    }

    @Override
    public double Eval() {
        return switch (operation) {
            case '-' -> -expr.Eval();
            default -> expr.Eval();
        };
    }

    @Override
    public String toString() {
        return String.format("%c %s", operation, expr);
    }
}
