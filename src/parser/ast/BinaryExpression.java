package parser.ast;

import lib.NumberValue;
import lib.StringValue;
import lib.Value;

public class BinaryExpression implements Expression {
    private final Expression expr1, expr2;
    private final char operation;


    public BinaryExpression(char operation, Expression expr1, Expression expr2) {
        this.operation = operation;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public Value eval() {
        final Value firstValue = expr1.eval();
        final Value secondValue = expr2.eval();

        if (firstValue instanceof StringValue) {
            final String firstString = firstValue.asString();

            return switch (operation) {
                case '*' -> {
                    final int iterations = (int) secondValue.asDouble();
                    yield new StringValue(String.valueOf(firstString).repeat(Math.max(0, iterations)));
                }
                default -> new StringValue(firstString + secondValue.asString());
            };

        }

        final double firstNumber = firstValue.asDouble();
        final double secondSecond = secondValue.asDouble();

        return switch (operation) {
            case '-' -> new NumberValue(firstNumber - secondSecond);
            case '*' -> new NumberValue(firstNumber * secondSecond);
            case '/' -> new NumberValue(firstNumber / secondSecond);
            default -> new NumberValue(firstNumber + secondSecond);
        };
    }

    @Override
    public String toString() {
        return String.format("%s %c %s", expr1, operation, expr2);
    }
}
