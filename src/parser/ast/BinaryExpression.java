package parser.ast;

public class BinaryExpression implements Expression {
    private final Expression expr1, expr2;
    private final char operation;


    public BinaryExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public double Eval() {
        return switch (operation) {
            case '-' -> expr1.Eval() - expr2.Eval();
            case '*' -> expr1.Eval() * expr2.Eval();
            case '/' -> expr1.Eval() / expr2.Eval();
            default -> expr1.Eval() + expr2.Eval();
        };
    }

    @Override
    public String toString() {
        return String.format("%s %c %s", expr1, operation, expr2);
    }
}
