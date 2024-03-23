package parser.ast;

public class NumberExpression implements Expression {
    private final double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public double Eval() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
